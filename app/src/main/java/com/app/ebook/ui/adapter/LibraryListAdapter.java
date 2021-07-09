package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemLibraryBinding;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.app.ebook.network.UrlConstants.BASE_URL3;

public class LibraryListAdapter extends RecyclerView.Adapter<LibraryListAdapter.ViewHolder> {

    private final Context mContext;
    private List<BookListResponse.ReturnResponseBean> data;
    private final LibraryListItemClickListener libraryListItemClickListener;

    public LibraryListAdapter(Context mContext, LibraryListItemClickListener libraryListItemClickListener) {
        this.mContext = mContext;
        this.libraryListItemClickListener = libraryListItemClickListener;
    }

    public void setData(ArrayList<BookListResponse.ReturnResponseBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_library, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemLibraryBinding itemViewBinding = holder.itemViewBinding;
        final BookListResponse.ReturnResponseBean dataBean = data.get(position);

        itemViewBinding.textViewBook.setText(dataBean.bookName);
        itemViewBinding.textViewSubject.setText(dataBean.subject);
        itemViewBinding.textViewClass.setText("Class " + AppUtilities.getClassName(mContext, dataBean.sclass));
        itemViewBinding.textViewBoard.setText(dataBean.board);
        itemViewBinding.textViewValidity.setText(dataBean.daysLeft);

        AppUtilities.loadImage(mContext, itemViewBinding.imageViewBook, BASE_URL3 + dataBean.coverPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                libraryListItemClickListener.onLibraryListItemClick(dataBean);
            }
        });

        itemViewBinding.imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                libraryListItemClickListener.onLibraryListItemMenuClick(view, dataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryBinding itemViewBinding;

        ViewHolder(@NonNull ItemLibraryBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }

    public interface LibraryListItemClickListener {
        void onLibraryListItemClick(BookListResponse.ReturnResponseBean returnResponseBean);

        void onLibraryListItemMenuClick(View view, BookListResponse.ReturnResponseBean returnResponseBean);
    }

}
