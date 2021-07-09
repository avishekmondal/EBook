package com.app.ebook.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ebook.R;
import com.app.ebook.databinding.ItemSubjectiveQuestionAnswerBinding;
import com.app.ebook.models.subjective.Answer;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.app.ebook.network.UrlConstants.BASE_URL2;

public class SubjectiveQuestionAnswersAdapter extends RecyclerView.Adapter<SubjectiveQuestionAnswersAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Answer> answerList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemSubjectiveQuestionAnswerBinding binder;

        public ViewHolder(ItemSubjectiveQuestionAnswerBinding binder) {
            super(binder.getRoot());
            this.binder = binder;
        }
    }

    public SubjectiveQuestionAnswersAdapter(Context context, ArrayList<Answer> answerList) {
        this.context = context;
        this.answerList = answerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubjectiveQuestionAnswerBinding binder = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_subjective_question_answer, parent, false);
        return new ViewHolder(binder);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectiveQuestionAnswersAdapter.ViewHolder holder, final int position) {
        if (!TextUtils.isEmpty(answerList.get(position).txt)) {
            holder.binder.contentText.setVisibility(View.VISIBLE);
            holder.binder.contentText.setText(answerList.get(position).txt);
        } else
            holder.binder.contentText.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(answerList.get(position).img))
            try {
                holder.binder.contentImage.setVisibility(View.VISIBLE);
                Glide
                        .with(context)
                        .load(BASE_URL2 + answerList.get(position).img)
                        .error(R.drawable.download)
                        .into(holder.binder.contentImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            holder.binder.contentImage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return (answerList != null ? answerList.size() : 0);
    }

}
