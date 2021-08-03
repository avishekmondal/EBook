package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemOrderBinding;
import com.app.ebook.models.cart.CartListResponse;
import com.app.ebook.util.AppUtilities;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context mContext;
    private List<CartListResponse.ReturnDataBean> data;

    public OrderAdapter(Context mContext, List<CartListResponse.ReturnDataBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_order, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemOrderBinding itemViewBinding = holder.itemViewBinding;
        final CartListResponse.ReturnDataBean dataBean = data.get(position);

        itemViewBinding.textViewBook.setText(dataBean.bookName);
        itemViewBinding.textViewDuration.setText("(" + dataBean.duration + " Months)");
        itemViewBinding.textViewPrice.setText(!dataBean.price.equals("0") ? "Rs. " + dataBean.price : "FREE");

        AppUtilities.loadImage(mContext, itemViewBinding.imageViewBook, dataBean.coverPhoto);

        if (position == getItemCount() - 1) {
            itemViewBinding.viewDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding itemViewBinding;

        ViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

}
