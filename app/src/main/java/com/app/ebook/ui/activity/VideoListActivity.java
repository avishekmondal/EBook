package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityVideoBinding;
import com.app.ebook.models.book_chapters.VideoListRequest;
import com.app.ebook.models.book_chapters.VideoListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.adapter.VideoListAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import hb.xvideoplayer.MxMediaManager;
import hb.xvideoplayer.MxUserAction;
import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;
import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.URL_PREVIEW_VIDEO_LIST;
import static com.app.ebook.network.UrlConstants.URL_VIDEO_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.IS_SUBSCRIBED;
import static hb.xvideoplayer.MxVideoPlayer.CURRENT_STATE_PAUSE;
import static hb.xvideoplayer.MxVideoPlayer.CURRENT_STATE_PLAYING;

public class VideoListActivity extends BaseActivity implements RetrofitListener, MxVideoPlayerWidget.UIStatusChangeListener {

    private ActivityVideoBinding binding;

    private RetroClient retroClient;
    public VideoListAdapter videoListAdapter;

    private MxVideoPlayerWidget videoPlayerWidget;
    private VideoListResponse.ReturnDataBean playingVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video);

        init();
        getVideoList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MxVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (MxVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        videoPlayerWidget = findViewById(R.id.videoView);
        videoPlayerWidget.setUIStatusListener(this);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickSubscribe(View view) {
        goToSubscriptionPlanActivity();
    }

    private void getVideoList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();

            VideoListRequest videoListRequest = new VideoListRequest();
            videoListRequest.bookId = mBookDetails.bookId;

            if (!mSessionManager.getBooleanSession(IS_SUBSCRIBED)) {
                binding.layoutSubscribeNow.setVisibility(View.VISIBLE);
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getPreviewVideoList(videoListRequest),
                        URL_PREVIEW_VIDEO_LIST);
            } else {
                binding.layoutSubscribeNow.setVisibility(View.GONE);
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getVideoList(videoListRequest),
                        URL_VIDEO_LIST);
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    public void startVideo(VideoListResponse.ReturnDataBean video) {
        if (playingVideo == null || !playingVideo.videoId.equals(video.videoId)) {
            playingVideo = video;
            videoPlayerWidget.autoStartPlay(
                    playingVideo.videoFile,
                    MxVideoPlayer.SCREEN_LAYOUT_NORMAL,
                    playingVideo.headingName + " - " + playingVideo.videoName
            );
        }
    }

    public void playVideo(boolean isPlay) {
        if (isPlay) {
            videoPlayerWidget.onActionEvent(MxUserAction.ON_CLICK_RESUME);
            MxMediaManager.getInstance().getPlayer().start();
            videoPlayerWidget.setUiPlayState(CURRENT_STATE_PLAYING);
        } else {
            videoPlayerWidget.onActionEvent(MxUserAction.ON_CLICK_PAUSE);
            MxMediaManager.getInstance().getPlayer().pause();
            videoPlayerWidget.setUiPlayState(CURRENT_STATE_PAUSE);
        }
    }

    public void scrollChapterList(final int position) {
        binding.recyclerViewChapterList.scrollToPosition(position);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        VideoListResponse videoListResponse = (VideoListResponse) response.body();
        if (videoListResponse != null && videoListResponse.retCode) {
            List<VideoListResponse.ReturnDataBean> videoList = videoListResponse.returnData;
            if (videoList.size() > 0) {
                videoListAdapter = new VideoListAdapter(VideoListActivity.this, videoList);
                binding.recyclerViewChapterList.setAdapter(videoListAdapter);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_videos));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_videos));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }

    @Override
    public void onUIChange(MxVideoPlayerWidget.Mode mode) {
        switch (mode) {
            case MODE_PLAYING:
                Log.v("aaModeChanges", "Play");
                break;
            case MODE_PAUSE:
                Log.v("aaModeChanges", "Pause");
                break;
            case MODE_COMPLETE:
                Log.v("aaModeChanges", "Complete");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        videoListAdapter.startNextVideo();
                    }
                }, 1000);
                break;
        }
    }
}