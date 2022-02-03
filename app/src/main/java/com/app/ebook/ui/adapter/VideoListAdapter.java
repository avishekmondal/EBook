package com.app.ebook.ui.adapter;

import static com.app.ebook.util.Constants.MEDIA_HEADING;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemMediaListBinding;
import com.app.ebook.models.book_chapters.VideoListResponse;
import com.app.ebook.ui.activity.VideoListActivity;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.SessionManager;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private final Context mContext;
    private final SessionManager mSessionManager;
    private final List<VideoListResponse.ReturnDataBean> data;
    private int selectedPosition = -1;
    private boolean isPlaying = false;

    public VideoListAdapter(Context mContext, List<VideoListResponse.ReturnDataBean> data) {
        this.mContext = mContext;
        this.mSessionManager = new SessionManager(mContext);
        this.data = data;

        String headingID = mSessionManager.getSession(MEDIA_HEADING);
        Log.v("aaheadingID", headingID);
        if (!headingID.equals("")) {
            for (int position = 0; position < data.size(); position++) {
                VideoListResponse.ReturnDataBean video = data.get(position);
                if (video.headingId.equals(headingID)) {
                    mSessionManager.setSession(MEDIA_HEADING, "");
                    selectedPosition = position;
                    if (selectedPosition < getItemCount() - 5) {
                        ((VideoListActivity) mContext).scrollChapterList(selectedPosition);
                    } else {
                        ((VideoListActivity) mContext).scrollChapterList(getItemCount() - 1);
                    }

                    startVideo(video);
                    break;
                }
            }
        }
    }

    public void startNextVideo() {
        if (selectedPosition < getItemCount() - 1) {
            selectedPosition++;
            startVideo(data.get(selectedPosition));
        } else {
            selectedPosition = -1;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMediaListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_media_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ItemMediaListBinding itemViewBinding = holder.itemViewBinding;
        final VideoListResponse.ReturnDataBean video = data.get(position);

        itemViewBinding.textViewMedia.setText(video.videoName);
        itemViewBinding.textViewDuration.setText(video.videoDuration);
        itemViewBinding.textViewChapter.setText(video.chapterName);
        itemViewBinding.textViewHeading.setText(video.headingName);
        AppUtilities.loadImage(mContext, itemViewBinding.imageViewMedia, video.videoCover);

        if (position == selectedPosition) {
            itemViewBinding.layoutMedia.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryOpacity25));
        } else {
            itemViewBinding.layoutMedia.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        itemViewBinding.cardViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != selectedPosition) {
                    selectedPosition = position;
                    startVideo(video);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMediaListBinding itemViewBinding;

        public ViewHolder(ItemMediaListBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

    private void startVideo(VideoListResponse.ReturnDataBean video) {
        ((VideoListActivity) mContext).startVideo(video);
    }

}
