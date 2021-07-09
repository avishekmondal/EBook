package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemSubscriptionPlanBinding;
import com.app.ebook.models.book_subscription.SubscriptionPlanListResponse;

import java.util.List;

public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanAdapter.ViewHolder> {

    private final Context mContext;
    private final List<SubscriptionPlanListResponse.ReturnDataBean> data;
    private final SubscriptionPlanListItemClickListener subscriptionPlanListItemClickListener;
    private int selectedPosition = -1;

    public SubscriptionPlanAdapter(Context mContext, List<SubscriptionPlanListResponse.ReturnDataBean> data,
                                   SubscriptionPlanListItemClickListener subscriptionPlanListItemClickListener) {
        this.mContext = mContext;
        this.data = data;
        this.subscriptionPlanListItemClickListener = subscriptionPlanListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriptionPlanBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_subscription_plan, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemSubscriptionPlanBinding itemViewBinding = holder.itemViewBinding;
        final SubscriptionPlanListResponse.ReturnDataBean dataBean = data.get(position);

        itemViewBinding.textViewPlan.setText(dataBean.plan);
        itemViewBinding.textViewDuration.setText( " (" + dataBean.duration + " Months)");
        itemViewBinding.textViewPrice.setText(!dataBean.price.equals("0") ? "Rs. " + dataBean.price : "FREE");

        if (dataBean.productType.equalsIgnoreCase("ebook")) {
            itemViewBinding.layoutSmartCoaching.setVisibility(View.GONE);
        } else {
            itemViewBinding.layoutSmartCoaching.setVisibility(View.VISIBLE);
        }

        if (position == selectedPosition) {
            itemViewBinding.layoutSelected.setVisibility(View.VISIBLE);
            itemViewBinding.textViewPrice.setBackgroundColor(mContext.getColor(R.color.blue_3C7AF0));
            itemViewBinding.textViewPrice.setTextColor(mContext.getColor(R.color.white));
        } else {
            itemViewBinding.layoutSelected.setVisibility(View.GONE);
            itemViewBinding.textViewPrice.setBackgroundColor(mContext.getColor(R.color.grey_EAEAEA));
            itemViewBinding.textViewPrice.setTextColor(mContext.getColor(R.color.black_333333));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != selectedPosition) {
                    selectedPosition = position;
                    subscriptionPlanListItemClickListener.onSubscriptionPlanListItemClick(dataBean);
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
        ItemSubscriptionPlanBinding itemViewBinding;

        ViewHolder(@NonNull ItemSubscriptionPlanBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

    public interface SubscriptionPlanListItemClickListener {
        void onSubscriptionPlanListItemClick(SubscriptionPlanListResponse.ReturnDataBean returnDataBean);
    }

}
