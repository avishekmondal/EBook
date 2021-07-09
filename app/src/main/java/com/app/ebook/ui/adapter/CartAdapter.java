package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemCartBinding;
import com.app.ebook.models.cart.CartListResponse;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.app.ebook.network.UrlConstants.BASE_URL2;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context mContext;
    private final CartItemClickListener cartItemClickListener;
    private List<CartListResponse.ReturnDataBean> data;

    public CartAdapter(Context mContext, CartItemClickListener cartItemClickListener) {
        this.mContext = mContext;
        this.cartItemClickListener = cartItemClickListener;
    }

    public void setData(List<CartListResponse.ReturnDataBean> data) {
        this.data = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            CartListResponse.ReturnDataBean returnDataBean = data.get(i);
            returnDataBean.isChecked = true;
            this.data.add(returnDataBean);
        }
        notifyDataSetChanged();
    }

    public List<CartListResponse.ReturnDataBean> getSelectedCartList() {
        List<CartListResponse.ReturnDataBean> selectedCartList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            CartListResponse.ReturnDataBean returnDataBean = data.get(i);
            if (returnDataBean.isChecked) {
                selectedCartList.add(returnDataBean);
            }
        }
        return selectedCartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_cart, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemCartBinding itemViewBinding = holder.itemViewBinding;
        final CartListResponse.ReturnDataBean dataBean = data.get(position);

        itemViewBinding.textViewBook.setText(dataBean.bookName);
        itemViewBinding.textViewSubject.setText(dataBean.subject);
        itemViewBinding.textViewClass.setText("Class " + AppUtilities.getClassName(mContext, dataBean.sclass));
        itemViewBinding.textViewBoard.setText(dataBean.board);
        itemViewBinding.textViewPrice.setText(!dataBean.price.equals("0") ? "Rs. " + dataBean.price : "FREE");
        itemViewBinding.checkBox.setChecked(dataBean.isChecked);

        AppUtilities.loadImage(mContext, itemViewBinding.imageViewBook, BASE_URL2 + dataBean.coverPhoto);

        if (position == getItemCount() - 1) {
            itemViewBinding.viewDivider.setVisibility(View.GONE);
        }

        itemViewBinding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                dataBean.isChecked = checked;
                notifyDataSetChanged();
            }
        });

        itemViewBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItemClickListener.onCartItemDeleteClick(dataBean.productId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding itemViewBinding;

        ViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

    public interface CartItemClickListener {
        void onCartItemDeleteClick(String productID);
    }

}
