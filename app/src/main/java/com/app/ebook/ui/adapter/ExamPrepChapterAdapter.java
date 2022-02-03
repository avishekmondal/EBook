package com.app.ebook.ui.adapter;

import static com.app.ebook.util.Constants.CHAPTER;
import static com.app.ebook.util.Constants.KEY;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemExamPrepChapterQuestionBinding;
import com.app.ebook.ui.activity.BaseActivity;
import com.app.ebook.ui.activity.ExamPrepMCQActivity;
import com.app.ebook.ui.activity.ExamPrepQuestionActivity;
import com.app.ebook.util.SessionManager;

import java.util.List;

public class ExamPrepChapterAdapter extends RecyclerView.Adapter<ExamPrepChapterAdapter.ViewHolder> {

    private final Context context;
    private final SessionManager mSessionManager;
    private final List<String> data;

    public ExamPrepChapterAdapter(Context context, List<String> data) {
        this.context = context;
        this.mSessionManager = new SessionManager(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExamPrepChapterQuestionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_exam_prep_chapter_question, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemExamPrepChapterQuestionBinding itemViewBinding = holder.itemViewBinding;
        itemViewBinding.textView.setText(data.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionManager.setSession(CHAPTER, data.get(position));
                if (mSessionManager.getSession(KEY).equalsIgnoreCase(context.getString(R.string.menu_mcq)))
                    ((BaseActivity) context).startTargetActivity(ExamPrepMCQActivity.class);
                else
                    ((BaseActivity) context).startTargetActivity(ExamPrepQuestionActivity.class);
                ((BaseActivity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemExamPrepChapterQuestionBinding itemViewBinding;

        ViewHolder(@NonNull ItemExamPrepChapterQuestionBinding binding) {
            super(binding.getRoot());
            itemViewBinding = binding;
        }
    }
}
