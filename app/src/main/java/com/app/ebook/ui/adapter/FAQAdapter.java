package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemFaqBinding;
import com.app.ebook.models.FAQResponse;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {

    private final Context mContext;
    private List<FAQResponse.ReturnResponse.Faqs> data;
    int selectedPosition = -1;

    public FAQAdapter(Context mContext, List<FAQResponse.ReturnResponse.Faqs> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_faq, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemFaqBinding itemViewBinding = holder.itemViewBinding;
        final FAQResponse.ReturnResponse.Faqs dataBean = data.get(position);

        itemViewBinding.textViewQuestion.setText("Q. " + dataBean.question);
        itemViewBinding.textViewAnswer.setText(dataBean.answer);

        if (position == selectedPosition) {
            itemViewBinding.layoutAnswer.setVisibility(View.VISIBLE);
        } else {
            itemViewBinding.layoutAnswer.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v-> {
            if (position != selectedPosition) {
                selectedPosition = position;
            } else {
                selectedPosition = -1;
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFaqBinding itemViewBinding;

        ViewHolder(@NonNull ItemFaqBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

}
