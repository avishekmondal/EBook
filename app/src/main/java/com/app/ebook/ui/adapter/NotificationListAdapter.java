package com.app.ebook.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemNotificationBinding;
import com.app.ebook.models.Book;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.BookTileViewHolder> {

//    private ArrayList<Book> list;
    private Context context;

    public NotificationListAdapter(/*ArrayList<Book> list,*/ Context context) {
//        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationListAdapter.BookTileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_notification,
                parent, false);
        return new BookTileViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.BookTileViewHolder holder, int position) {
//        holder.initUi(list.get(position));
    }

    @Override
    public int getItemCount() {
        return /*list.size()*/10;
    }

    public class BookTileViewHolder extends RecyclerView.ViewHolder {
        private ItemNotificationBinding binding;

        public BookTileViewHolder(ItemNotificationBinding binding, NotificationListAdapter bookAdapter) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void initUi(Book book) {
            /*AppUtilities.imageLoadGlide(context, book.getPhotoUrl()"https://i.ibb.co/db5QQmm/back-png.png", binding.imageViewPic);
            binding.textViewBookName.setText(book.getBook());
            binding.textViewWriterName.setText("Writer : " + book.getWriter());
            binding.className.setText("Class : " + book.getEduClass() + ", " + book.getBoard());
            binding.bookType.setText("Book Type : " + book.getBookType());*/
        }


    }
}
