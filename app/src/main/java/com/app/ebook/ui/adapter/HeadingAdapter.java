package com.app.ebook.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemHeadingBinding;
import com.app.ebook.models.book_chapters.Heading;
import com.app.ebook.ui.activity.AudioListActivity;
import com.app.ebook.ui.activity.BaseActivity;
import com.app.ebook.ui.activity.VideoListActivity;
import com.app.ebook.util.SessionManager;

import java.util.List;

import static com.app.ebook.util.Constants.MEDIA_CHAPTER;
import static com.app.ebook.util.Constants.MEDIA_HEADING;

public class HeadingAdapter extends RecyclerView.Adapter<HeadingAdapter.ViewHolder> {

    private final Context mContext;
    private final SessionManager mSessionManager;
    private final List<Heading> data;
    private int selectedPosition = 0;

    public HeadingAdapter(Context mContext, List<Heading> data) {
        this.mContext = mContext;
        this.mSessionManager = new SessionManager(mContext);
        this.data = data;
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

        if (position == selectedPosition) {
            itemViewBinding.layoutButtons.setVisibility(View.VISIBLE);
        } else {
            itemViewBinding.layoutButtons.setVisibility(View.GONE);
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

        itemViewBinding.buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionManager.setSession(MEDIA_CHAPTER, heading.chapterIdId);
                mSessionManager.setSession(MEDIA_HEADING, heading.headingId);
                ((BaseActivity) mContext).startTargetActivity(AudioListActivity.class);
                ((BaseActivity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        itemViewBinding.buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionManager.setSession(MEDIA_CHAPTER, heading.chapterIdId);
                mSessionManager.setSession(MEDIA_HEADING, heading.headingId);
                ((BaseActivity) mContext).startTargetActivity(VideoListActivity.class);
                ((BaseActivity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        /*holder.binder.buttonReadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, EBookHeadingActivity.class));
            }
        });*/
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
