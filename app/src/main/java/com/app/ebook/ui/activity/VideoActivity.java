package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityVideoBinding;
import com.app.ebook.models.book_chapters.BookChapterListRequest;
import com.app.ebook.models.book_chapters.BookChapterListResponse;
import com.app.ebook.models.book_chapters.ReturnData;
import com.app.ebook.models.book_chapters.Videos;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.adapter.MediaChapterAdapter;
import com.app.ebook.ui.adapter.VideoAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import hb.xvideoplayer.MxMediaManager;
import hb.xvideoplayer.MxUserAction;
import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;
import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.URL_BOOK_VIDEO_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.IS_MEDIA_PLAYING;
import static hb.xvideoplayer.MxVideoPlayer.CURRENT_STATE_PAUSE;
import static hb.xvideoplayer.MxVideoPlayer.CURRENT_STATE_PLAYING;

public class VideoActivity extends BaseActivity implements RetrofitListener, MxVideoPlayerWidget.UIStatusChangeListener {

    private ActivityVideoBinding binding;

    private RetroClient retroClient;
    public VideoAdapter videoAdapter;

    private MxVideoPlayerWidget videoPlayerWidget;
    private Videos playingVideo;

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

        mSessionManager.setSession(IS_MEDIA_PLAYING, true);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    private void getVideoList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();

            BookChapterListRequest bookChapterListRequest = new BookChapterListRequest();
            bookChapterListRequest.bookId = mBookDetails.bookId;
            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getBookVideoList(bookChapterListRequest),
                    URL_BOOK_VIDEO_LIST);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    public void startVideo(Videos video, String heading) {
        if (playingVideo == null || !playingVideo.videoId.equals(video.videoId)) {
            playingVideo = video;
            videoPlayerWidget.autoStartPlay(
                    playingVideo.videoFile,
                    MxVideoPlayer.SCREEN_LAYOUT_NORMAL,
                    heading + " - Video " + playingVideo.videoId
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
        binding.recyclerViewChapterList.post(new Runnable() {
            @Override
            public void run() {
                ((LinearLayoutManager) binding.recyclerViewChapterList.getLayoutManager()).scrollToPositionWithOffset(position, 0);
            }
        });
    }

    /*public void scrollChapterList(final View view) {
        binding.scrollView.scrollTo(0, (int)view.getY());
        //view.getParent().requestChildFocus(view,view);
    }*/

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        BookChapterListResponse chapterListResponse = (BookChapterListResponse) response.body();
        if (chapterListResponse != null && chapterListResponse.retCode) {
            List<ReturnData> chapterList = chapterListResponse.returnData;
            if (chapterList.size() > 0) {
                final MediaChapterAdapter mediaChapterAdapter = new MediaChapterAdapter(VideoActivity.this, chapterList);
                binding.recyclerViewChapterList.setAdapter(mediaChapterAdapter);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_chapters));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_chapters));
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
                mSessionManager.setSession(IS_MEDIA_PLAYING, true);
                videoAdapter.updateVideoAdapter();
                break;
            case MODE_PAUSE:
                Log.v("aaModeChanges", "Pause");
                mSessionManager.setSession(IS_MEDIA_PLAYING, false);
                videoAdapter.updateVideoAdapter();
                break;
            case MODE_COMPLETE:
                Log.v("aaModeChanges", "Complete");
                mSessionManager.setSession(IS_MEDIA_PLAYING, false);
                videoAdapter.updateVideoAdapter();
                break;
        }
    }
}