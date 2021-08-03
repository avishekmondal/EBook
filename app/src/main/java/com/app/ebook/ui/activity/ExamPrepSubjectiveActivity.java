package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityExamPrepSubjectiveBinding;
import com.app.ebook.models.subjective.Answer;
import com.app.ebook.models.subjective.ReturnData;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.app.ebook.util.Constants.CHAPTER;

public class ExamPrepSubjectiveActivity extends BaseActivity {

    public static final String SUBJECTIVE_LIST_EXTRA = "SubjectiveListResponseExtra";
    public static final String SUBJECTIVE_POSITION_EXTRA = "SubjectivePositionExtra";

    private ActivityExamPrepSubjectiveBinding binding;

    private List<ReturnData> subjectiveList = new ArrayList<>();
    private int subjectivePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_prep_subjective);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        binding.textViewTitle.setText(mSessionManager.getSession(CHAPTER));

        subjectiveList = (List<ReturnData>) getIntent().getSerializableExtra(SUBJECTIVE_LIST_EXTRA);
        subjectivePosition = getIntent().getIntExtra(SUBJECTIVE_POSITION_EXTRA, 0);

        setSubjectiveList(subjectivePosition);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickPrevious(View view) {
        if (subjectivePosition == 0) {
            //AppUtilities.showToast(this, "No Previous Question available");
        } else {
            setSubjectiveList(subjectivePosition = subjectivePosition - 1);
        }
    }

    public void onClickNext(View view) {
        if (subjectivePosition == subjectiveList.size() - 1) {
            //AppUtilities.showToast(this, "No More Question available");
        } else {
            setSubjectiveList(subjectivePosition = subjectivePosition + 1);
        }
    }

    private void setSubjectiveList(int subjectivePosition) {
        // Question
        if (subjectiveList.get(subjectivePosition).question.txt != null && !TextUtils.isEmpty(subjectiveList.get(subjectivePosition).question.txt))
            binding.textViewQuestion.setText("Q. " + subjectiveList.get(subjectivePosition).question.txt);
        if (subjectiveList.get(subjectivePosition).question.img != null && !TextUtils.isEmpty(subjectiveList.get(subjectivePosition).question.img))
            loadImage(binding.imageViewQuestion, subjectiveList.get(subjectivePosition).question.img);
        else
            binding.imageViewQuestion.setVisibility(View.GONE);

        // Answer
        List<Answer> answerList = subjectiveList.get(subjectivePosition).answer;
        for (int i = 0; i < answerList.size(); i++) { // Text
            if (answerList.get(i).txt != null && !TextUtils.isEmpty(answerList.get(i).txt)) {
                TextView textView = new TextView(this);
                textView.setText(binding.layoutAnswer.getChildCount() == 0  ? answerList.get(i).txt :
                        "\n" + answerList.get(i).txt);
                textView.setTextColor(getColor(R.color.black_646464));
                textView.setTextSize(14);
                textView.setLineSpacing(getResources().getDimension(R.dimen._1sdp), 1.2f);
                binding.layoutAnswer.addView(textView);
            } else if (answerList.get(i).img != null && !TextUtils.isEmpty(answerList.get(i).img)) { // Image
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(750, 500);
                layoutParams.setMargins(0, 25,0, 15);
                layoutParams.gravity = Gravity.CENTER;
                imageView.setLayoutParams(layoutParams);
                AppUtilities.loadImage(this, imageView, answerList.get(i).img);
                binding.layoutAnswer.addView(imageView);
            }
        }
    }

    private void loadImage(ImageView imageView, String imagePath) {
        imageView.setVisibility(View.VISIBLE);
        AppUtilities.loadImage(this, imageView, imagePath);
    }

}