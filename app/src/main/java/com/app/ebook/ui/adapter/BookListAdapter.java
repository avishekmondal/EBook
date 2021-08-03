package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemBookBinding;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private final Context mContext;
    private List<BookListResponse.ReturnResponseBean> data;
    private final BookListAdapter.BookListItemClickListener bookListItemClickListener;

    public BookListAdapter(Context mContext, BookListItemClickListener bookListItemClickListener) {
        this.mContext = mContext;
        this.bookListItemClickListener = bookListItemClickListener;
    }

    public void setData(ArrayList<BookListResponse.ReturnResponseBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_book, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemBookBinding itemViewBinding = holder.itemViewBinding;
        final BookListResponse.ReturnResponseBean dataBean = data.get(position);

        itemViewBinding.textViewBook.setText(dataBean.bookName);
        itemViewBinding.textViewSubject.setText(dataBean.subject);
        itemViewBinding.textViewClass.setText("Class " + AppUtilities.getClassName(mContext, dataBean.sclass));
        itemViewBinding.textViewBoard.setText(dataBean.board);
        //itemViewBinding.textViewPrice.setText("Rs. " + dataBean.bookPrice);

        AppUtilities.loadImage(mContext, itemViewBinding.imageViewBook, dataBean.coverPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookListItemClickListener.onBookListItemClick(dataBean);
            }
        });

        itemViewBinding.imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookListItemClickListener.onBookListItemMenuClick(view, dataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBookBinding itemViewBinding;

        ViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

    public interface BookListItemClickListener {
        void onBookListItemClick(BookListResponse.ReturnResponseBean returnResponseBean);

        void onBookListItemMenuClick(View view, BookListResponse.ReturnResponseBean returnResponseBean);
    }

}
