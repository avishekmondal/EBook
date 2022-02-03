package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemChatBinding;
import com.app.ebook.models.chat.ChatResponse;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final Context mContext;
    private List<ChatResponse.ReturnResponse> data = new ArrayList<>();

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ChatResponse.ReturnResponse> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setData(ChatResponse.ReturnResponse data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_chat, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemChatBinding itemViewBinding = holder.itemViewBinding;
        final ChatResponse.ReturnResponse dataBean = data.get(position);

        if (dataBean.username.equalsIgnoreCase("Admin")) {
            itemViewBinding.layoutAdmin.setVisibility(View.VISIBLE);
            itemViewBinding.layoutUser.setVisibility(View.GONE);
            itemViewBinding.textViewAdminMessage.setText(dataBean.text);
            itemViewBinding.textViewAdminTime.setText(AppUtilities.convertDateFormat_String(dataBean.time,
                    "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy  HH:mm a"));
        } else {
            itemViewBinding.layoutAdmin.setVisibility(View.GONE);
            itemViewBinding.layoutUser.setVisibility(View.VISIBLE);
            //itemViewBinding.textViewUserName.setText(AppUtilities.getUser(mContext).name);
            itemViewBinding.textViewUserMessage.setText(dataBean.text);
            itemViewBinding.textViewUserTime.setText(AppUtilities.convertDateFormat_String(dataBean.time,
                    "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy  HH:mm a"));
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemChatBinding itemViewBinding;

        ViewHolder(@NonNull ItemChatBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

}
