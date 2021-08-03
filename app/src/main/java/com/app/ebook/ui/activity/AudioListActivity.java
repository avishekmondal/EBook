package com.app.ebook.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityAudioBinding;
import com.app.ebook.models.book_chapters.AudioListRequest;
import com.app.ebook.models.book_chapters.AudioListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.adapter.AudioListAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.URL_AUDIO_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.BOOK_ID;

public class AudioListActivity extends BaseActivity implements RetrofitListener,
        MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private ActivityAudioBinding binding;

    private RetroClient retroClient;
    public AudioListAdapter audioListAdapter;

    private AudioListResponse.ReturnDataBean playingAudio;
    private MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio);

        init();
        getAudioList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mediaPlayer.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        binding.seekBar.setOnSeekBarChangeListener(this);
    }

    public void onClickBackward(View view) {
        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        } else {
            // backward to starting position
            mediaPlayer.seekTo(0);
        }
    }

    public void onClickForward(View view) {
        // get current song position
        int currentPosition = mediaPlayer.getCurrentPosition();
        // check if seekForward time is lesser than song duration
        if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
            // forward song
            mediaPlayer.seekTo(currentPosition + seekForwardTime);
        } else {
            // forward to end position
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }
    }

    public void onClickPlay(View view) {
        playAudio(!mediaPlayer.isPlaying());
    }

    public void playAudio(boolean isPlay) {
        if (isPlay) {
            mediaPlayer.start();
            binding.buttonPlay.setImageResource(R.drawable.img_pause);
        } else {
            mediaPlayer.pause();
            binding.buttonPlay.setImageResource(R.drawable.img_play);
        }
    }

    public void startAudio(final AudioListResponse.ReturnDataBean audio) {
        if (playingAudio == null || !playingAudio.audioId.equals(audio.audioId)) {
            playingAudio = audio;
            binding.textViewMedia.setText(audio.headingName + " - " + audio.audioName);
            binding.buttonPlay.setImageResource(R.drawable.img_pause);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(audio.audioFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        binding.seekBar.setProgress(0);
                        binding.seekBar.setMax(100);

                        updateProgressBar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 300);
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            binding.textViewTotalDuration.setText("" + milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            binding.textViewCurrentDuration.setText("" + milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            binding.seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onCompletion(MediaPlayer arg0) {
        binding.buttonPlay.setImageResource(R.drawable.img_play);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                audioListAdapter.startNextAudio();
            }
        }, 1000);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    private void getAudioList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();

            AudioListRequest audioListRequest = new AudioListRequest();
            audioListRequest.bookId = mSessionManager.getSession(BOOK_ID);
            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getAudioList(audioListRequest),
                    URL_AUDIO_LIST);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    public void scrollChapterList(final int position) {
        binding.recyclerViewChapterList.scrollToPosition(position);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        AudioListResponse audioListResponse = (AudioListResponse) response.body();
        if (audioListResponse != null && audioListResponse.retCode) {
            List<AudioListResponse.ReturnDataBean> audioList = audioListResponse.returnData;
            if (audioList.size() > 0) {
                audioListAdapter = new AudioListAdapter(AudioListActivity.this, audioList);
                binding.recyclerViewChapterList.setAdapter(audioListAdapter);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_audios));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_audios));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }
}