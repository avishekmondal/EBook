package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemFaqCategoryBinding;
import com.app.ebook.models.FAQResponse;

import java.util.List;

public class FAQCategoryAdapter extends RecyclerView.Adapter<FAQCategoryAdapter.ViewHolder> {

    private final Context mContext;
    private final FAQCategoryItemClickListener faqCategoryItemClickListener;
    private List<FAQResponse.ReturnResponse> data;

    public FAQCategoryAdapter(Context mContext, FAQCategoryItemClickListener faqCategoryItemClickListener) {
        this.mContext = mContext;
        this.faqCategoryItemClickListener = faqCategoryItemClickListener;
    }

    public void setData(List<FAQResponse.ReturnResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_faq_category, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemFaqCategoryBinding itemViewBinding = holder.itemViewBinding;
        final FAQResponse.ReturnResponse dataBean = data.get(position);

        itemViewBinding.textViewFaqCategory.setText(dataBean.category);

        holder.itemView.setOnClickListener(v -> {
            faqCategoryItemClickListener.onFAQCategoryItemClick(dataBean);
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFaqCategoryBinding itemViewBinding;

        ViewHolder(@NonNull ItemFaqCategoryBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

    public interface FAQCategoryItemClickListener {
        void onFAQCategoryItemClick(FAQResponse.ReturnResponse returnResponse);
    }

}
