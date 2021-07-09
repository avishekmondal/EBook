package com.app.ebook.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemExamPrepChapterQuestionBinding;
import com.app.ebook.models.subjective.ReturnData;
import com.app.ebook.ui.activity.BaseActivity;
import com.app.ebook.ui.activity.ExamPrepSubjectiveActivity;

import java.io.Serializable;
import java.util.List;

public class ExamPrepQuestionAdapter extends RecyclerView.Adapter<ExamPrepQuestionAdapter.ViewHolder> {

    private final Context mContext;
    private List<ReturnData> data;

    public ExamPrepQuestionAdapter(Context mContext, List<ReturnData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExamPrepChapterQuestionBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext), R.layout.item_exam_prep_chapter_question, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamPrepQuestionAdapter.ViewHolder holder, final int position) {
        final ItemExamPrepChapterQuestionBinding itemViewBinding = holder.itemViewBinding;
        itemViewBinding.textView.setText("Q. " + data.get(position).question.txt);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ExamPrepSubjectiveActivity.SUBJECTIVE_LIST_EXTRA, (Serializable) data);
                bundle.putInt(ExamPrepSubjectiveActivity.SUBJECTIVE_POSITION_EXTRA, position);
                ((BaseActivity) mContext).startTargetActivity(ExamPrepSubjectiveActivity.class, bundle);
                ((BaseActivity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
