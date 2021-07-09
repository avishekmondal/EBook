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
import com.app.ebook.databinding.ItemMediaBinding;
import com.app.ebook.models.book_chapters.Audios;
import com.app.ebook.ui.activity.AudioActivity;
import com.app.ebook.util.SessionManager;

import java.util.List;

import static com.app.ebook.util.Constants.IS_MEDIA_PLAYING;
import static com.app.ebook.util.Constants.MEDIA_AUDIO;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    private final Context mContext;
    private final SessionManager mSessionManager;
    private final List<Audios> data;
    private final String heading;
    private int selectedPosition = -1;
    private boolean isPlaying = false;

    public AudioAdapter(Context mContext, List<Audios> data, String heading) {
        this.mContext = mContext;
        this.mSessionManager = new SessionManager(mContext);
        this.data = data;
        this.heading = heading;

        String audioID = mSessionManager.getSession(MEDIA_AUDIO);
        if (!audioID.equals("")) {
            for (int position = 0; position < data.size(); position++) {
                Audios audio = data.get(position);
                if (audio.audioId.equals(audioID)) {
                    selectedPosition = position;
                    isPlaying = mSessionManager.getBooleanSession(IS_MEDIA_PLAYING);
                    startAudio(audio);
                    break;
                }
            }
        }
    }

    public void updateAudioAdapter() {
        isPlaying = mSessionManager.getBooleanSession(IS_MEDIA_PLAYING);
        Log.v("aa", ""+isPlaying);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMediaBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_media, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemMediaBinding itemViewBinding = holder.itemViewBinding;
        final Audios audio = data.get(position);

        itemViewBinding.textViewMedia.setText("Audio " + audio.audioId);

        if (position == selectedPosition) {
            if (isPlaying) {
                itemViewBinding.imageViewAction.setImageResource(R.drawable.ic_action_pause);
            } else {
                itemViewBinding.imageViewAction.setImageResource(R.drawable.ic_action_play);
            }
        } else {
            itemViewBinding.imageViewAction.setImageResource(R.drawable.ic_action_play);
        }

        itemViewBinding.cardViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != selectedPosition) {
                    selectedPosition = position;
                    mSessionManager.setSession(MEDIA_AUDIO, audio.audioId);
                    mSessionManager.setSession(IS_MEDIA_PLAYING, isPlaying = true);
                    startAudio(audio);
                } else {
                    isPlaying = !isPlaying;
                    ((AudioActivity) mContext).playAudio(isPlaying);
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
        ItemMediaBinding itemViewBinding;

        public ViewHolder(ItemMediaBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

    private void startAudio(Audios audio) {
        Log.v("aaaudioID", audio.audioId);
        ((AudioActivity) mContext).startAudio(audio, heading);
    }

}
