package com.app.ebook.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.ui.activity.BookDetailsActivity;

public class BookHorizontalListAdapter extends RecyclerView.Adapter<BookHorizontalListAdapter.BookTileViewHolder> {

    //    private ArrayList<String> list;
    private Context context;

    public BookHorizontalListAdapter(/*ArrayList<String> list,*/ Context context) {
//        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BookHorizontalListAdapter.BookTileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_tile, null);
        return new BookTileViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHorizontalListAdapter.BookTileViewHolder holder, int position) {
        /*AppUtilities.imageLoadGlide(context, *//*list.get(position)*//*"https://i.ibb.co/db5QQmm/back-png.png", holder.imageView);*/
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, BookDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return /*list.size()*/5;
    }

    public class BookTileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BookTileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
