package com.app.ebook.ui.adapter;

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
import com.app.ebook.models.book_chapters.AudioListResponse;
import com.app.ebook.ui.activity.AudioListActivity;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.SessionManager;

import java.util.List;

import static com.app.ebook.network.UrlConstants.BASE_URL3;
import static com.app.ebook.util.Constants.MEDIA_HEADING;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {

    private final Context mContext;
    private final SessionManager mSessionManager;
    private final List<AudioListResponse.ReturnDataBean> data;
    private int selectedPosition = -1;

    public AudioListAdapter(Context mContext, List<AudioListResponse.ReturnDataBean> data) {
        this.mContext = mContext;
        this.mSessionManager = new SessionManager(mContext);
        this.data = data;

        String headingID = mSessionManager.getSession(MEDIA_HEADING);
        Log.v("aaheadingID", headingID);
        if (!headingID.equals("")) {
            for (int position = 0; position < data.size(); position++) {
                AudioListResponse.ReturnDataBean audio = data.get(position);
                if (audio.headingId.equals(headingID)) {
                    mSessionManager.setSession(MEDIA_HEADING, "");
                    selectedPosition = position;
                    if (selectedPosition < getItemCount() - 5) {
                        ((AudioListActivity) mContext).scrollChapterList(selectedPosition);
                    } else {
                        ((AudioListActivity) mContext).scrollChapterList(getItemCount() - 1);
                    }

                    startAudio(audio);
                    break;
                }
            }
        }
    }

    public void startNextAudio() {
        if (selectedPosition < getItemCount() - 1) {
            selectedPosition++;
            startAudio(data.get(selectedPosition));
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
        final AudioListResponse.ReturnDataBean audio = data.get(position);

        itemViewBinding.textViewMedia.setText(audio.audioName);
        itemViewBinding.textViewDuration.setText(audio.audioDuration);
        itemViewBinding.textViewChapter.setText(audio.chapterName);
        itemViewBinding.textViewHeading.setText(audio.headingName);
        AppUtilities.loadImage(mContext, itemViewBinding.imageViewMedia, BASE_URL3 + audio.audioCover);

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
                    startAudio(audio);
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

    private void startAudio(AudioListResponse.ReturnDataBean audio) {
        ((AudioListActivity) mContext).startAudio(audio);
    }

}
