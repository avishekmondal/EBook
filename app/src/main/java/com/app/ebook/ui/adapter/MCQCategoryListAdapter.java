package com.app.ebook.ui.adapter;

import static com.app.ebook.util.Constants.KEY;
import static com.app.ebook.util.Constants.MCQ_CATEGORY;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemMcqCategoryBinding;
import com.app.ebook.ui.activity.BaseActivity;
import com.app.ebook.ui.activity.ExamPrepChapterActivity;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.SessionManager;

import java.util.List;

public class MCQCategoryListAdapter extends RecyclerView.Adapter<MCQCategoryListAdapter.ViewHolder> {

    private final Context context;
    private final List<String> data;
    private SessionManager mSessionManager;

    public MCQCategoryListAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        this.mSessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMcqCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_mcq_category, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemMcqCategoryBinding itemViewBinding = holder.itemViewBinding;
        itemViewBinding.textView.setText(AppUtilities.toCamelCase(data.get(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionManager.setSession(KEY, context.getString(R.string.menu_mcq));
                mSessionManager.setSession(MCQ_CATEGORY, data.get(position));
                ((BaseActivity) context).startTargetActivity(ExamPrepChapterActivity.class);
                ((BaseActivity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMcqCategoryBinding itemViewBinding;

        ViewHolder(@NonNull ItemMcqCategoryBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }


}
