package com.app.ebook.ui.adapter;

import static com.app.ebook.util.Constants.MEDIA_AUDIO;
import static com.app.ebook.util.Constants.MEDIA_HEADING;
import static com.app.ebook.util.Constants.MEDIA_VIDEO;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemHeadingBinding;
import com.app.ebook.models.book_chapters.Heading;
import com.app.ebook.ui.activity.AudioActivity;
import com.app.ebook.ui.activity.VideoActivity;
import com.app.ebook.util.SessionManager;

import java.util.List;

public class MediaHeadingAdapter extends RecyclerView.Adapter<MediaHeadingAdapter.ViewHolder> {

    private final Context mContext;
    private final SessionManager mSessionManager;
    private final List<Heading> data;
    private int selectedPosition = 0;

    private AudioAdapter audioAdapter;
    private VideoAdapter videoAdapter;

    public MediaHeadingAdapter(Context mContext, List<Heading> data) {
        this.mContext = mContext;
        this.mSessionManager = new SessionManager(mContext);
        this.data = data;

        String headingID = mSessionManager.getSession(MEDIA_HEADING);
        Log.v("aaheadingID", headingID);
        if (!headingID.equals("")) {
            for (int position = 0; position < data.size(); position++) {
                Heading heading = data.get(position);
                if (heading.headingId.equals(headingID)) {
                    selectedPosition = position;
                    mSessionManager.setSession(MEDIA_HEADING, "");
                    if (mContext instanceof AudioActivity) {
                        mSessionManager.setSession(MEDIA_AUDIO, heading.audios.get(0).audioId);
                    } else {
                        mSessionManager.setSession(MEDIA_VIDEO, heading.videos.get(0).videoId);
                    }
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHeadingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_heading, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemHeadingBinding itemViewBinding = holder.itemViewBinding;
        final Heading heading = data.get(position);

        itemViewBinding.textViewHeading.setText(heading.headingName);

        if (!TextUtils.isEmpty(heading.subheading)) {
            itemViewBinding.textViewSubHeading.setVisibility(View.VISIBLE);
            itemViewBinding.textViewSubHeading.setText(heading.subheading);
        } else {
            itemViewBinding.textViewSubHeading.setVisibility(View.GONE);
        }

        itemViewBinding.layoutButtons.setVisibility(View.GONE);

        if (mContext instanceof AudioActivity) {
            audioAdapter = new AudioAdapter(mContext, heading.audios, heading.headingName);
            itemViewBinding.recyclerViewMediaList.setAdapter(audioAdapter);
        } else {
            videoAdapter = new VideoAdapter(mContext, heading.videos, heading.headingName);
            itemViewBinding.recyclerViewMediaList.setAdapter(videoAdapter);
        }

        if (position == selectedPosition) {
            if (mContext instanceof  AudioActivity) {
                ((AudioActivity) mContext).audioAdapter = audioAdapter;
            } else {
                ((VideoActivity) mContext).videoAdapter = videoAdapter;
            }
            itemViewBinding.recyclerViewMediaList.setVisibility(View.VISIBLE);
        } else {
            itemViewBinding.recyclerViewMediaList.setVisibility(View.GONE);
        }

        itemViewBinding.cardViewHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != selectedPosition) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
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
        ItemHeadingBinding itemViewBinding;

        public ViewHolder(ItemHeadingBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

}
