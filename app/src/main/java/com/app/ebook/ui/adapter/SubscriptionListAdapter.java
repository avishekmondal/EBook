package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemSubscriptionBinding;
import com.app.ebook.models.book_subscription.SubscriptionListResponse;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.app.ebook.network.UrlConstants.BASE_URL2;

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.ViewHolder> {

    private final Context mContext;
    private List<SubscriptionListResponse.ReturnDataBean> data;

    public SubscriptionListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(ArrayList<SubscriptionListResponse.ReturnDataBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriptionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_subscription, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemSubscriptionBinding itemViewBinding = holder.itemViewBinding;
        final SubscriptionListResponse.ReturnDataBean dataBean = data.get(position);

        itemViewBinding.textViewBook.setText(dataBean.bookName);
        itemViewBinding.textViewSubject.setText(dataBean.subject);
        itemViewBinding.textViewDate.setText(AppUtilities.convertDateFormat_String(dataBean.startDate,
                "yyyy-MM-dd", "dd-MM-yyyy"));
        itemViewBinding.textViewValidity.setText("Valid till " + AppUtilities.convertDateFormat_String(dataBean.endDate,
                "yyyy-MM-dd", "dd MMM, yyyy"));
        itemViewBinding.textViewPrice.setText(!dataBean.price.equals("0") ? "Rs. " + dataBean.price : "FREE");
        itemViewBinding.textViewPlan.setText("(" + dataBean.plan + ")");

        AppUtilities.loadImage(mContext, itemViewBinding.imageViewBook, BASE_URL2 + dataBean.coverPhoto);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSubscriptionBinding itemViewBinding;

        ViewHolder(@NonNull ItemSubscriptionBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }
}
