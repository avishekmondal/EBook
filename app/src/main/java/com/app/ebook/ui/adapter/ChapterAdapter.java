package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemChapterBinding;
import com.app.ebook.models.book_chapters.ReturnData;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private final Context mContext;
    private final List<ReturnData> data;
    private int selectedPosition = 0;

    public ChapterAdapter(Context mContext, List<ReturnData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_chapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemChapterBinding itemViewBinding = holder.itemViewBinding;
        final ReturnData returnData = data.get(position);

        itemViewBinding.textViewChapter.setText((position + 1) + ". " + returnData.chapterName);

        HeadingAdapter headingAdapter = new HeadingAdapter(mContext, returnData.heading);
        itemViewBinding.recyclerViewHeadingList.setAdapter(headingAdapter);

        if (position == selectedPosition) {
            itemViewBinding.imageViewDropStatus.setImageResource(R.drawable.ic_action_dropup);
            itemViewBinding.recyclerViewHeadingList.setVisibility(View.VISIBLE);
        } else {
            itemViewBinding.imageViewDropStatus.setImageResource(R.drawable.ic_action_dropdown);
            itemViewBinding.recyclerViewHeadingList.setVisibility(View.GONE);
        }

        itemViewBinding.layoutChapter.setOnClickListener(new View.OnClickListener() {
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
        ItemChapterBinding itemViewBinding;

        ViewHolder(@NonNull ItemChapterBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }
}
