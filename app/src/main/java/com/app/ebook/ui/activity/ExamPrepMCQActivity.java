package com.app.ebook.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityExamPrepMCQBinding;
import com.app.ebook.models.mcq.MCQListRequest;
import com.app.ebook.models.mcq.MCQListResponse;
import com.app.ebook.models.mcq.ReturnData;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.BASE_URL2;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.BOOK_ID;
import static com.app.ebook.util.Constants.CHAPTER;

public class ExamPrepMCQActivity extends BaseActivity implements RetrofitListener {

    private ActivityExamPrepMCQBinding binding;
    private RetroClient retroClient;

    private ArrayList<ReturnData> mcqList;
    private int mcqPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_prep_m_c_q);

        init();
        getQuestionList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        binding.textViewTitle.setText(mSessionManager.getSession(CHAPTER));
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickPrevious(View view) {
        if (mcqPosition == 0) {
            //AppUtilities.showToast(this, "No Previous MCQ available");
        } else {
            disableRadioButtons();
            setQuestionList(mcqPosition = mcqPosition - 1);
        }
    }

    public void onClickNext(View view) {
        if (mcqPosition == mcqList.size() - 1) {
            //AppUtilities.showToast(this, "No More MCQ available");
        } else {
            disableRadioButtons();
            setQuestionList(mcqPosition = mcqPosition + 1);
        }
    }

    private void setQuestionList(int mcqPosition) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 300);
        enableRadioButtons();

        // Question
        if (mcqList.get(mcqPosition).question.txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).question.txt))
            binding.textViewQuestion.setText("Q. " + mcqList.get(mcqPosition).question.txt);
        if (mcqList.get(mcqPosition).question.img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).question.img))
            loadImage(binding.imageViewQuestion, mcqList.get(mcqPosition).question.img);
        else
            binding.imageViewQuestion.setVisibility(View.GONE);

        // Options
        if (mcqList.get(mcqPosition).options.get(0).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(0).txt))
            binding.radioButtonOption1.setText(mcqList.get(mcqPosition).options.get(0).txt);
        if (mcqList.get(mcqPosition).options.get(0).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(0).img))
            loadImage(binding.imageViewOption1, mcqList.get(mcqPosition).options.get(0).img);
        else
            binding.imageViewOption1.setVisibility(View.GONE);

        if (mcqList.get(mcqPosition).options.get(1).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(1).txt))
            binding.radioButtonOption2.setText(mcqList.get(mcqPosition).options.get(1).txt);
        if (mcqList.get(mcqPosition).options.get(1).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(1).img))
            loadImage(binding.imageViewOption2, mcqList.get(mcqPosition).options.get(1).img);
        else
            binding.imageViewOption2.setVisibility(View.GONE);

        if (mcqList.get(mcqPosition).options.get(2).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(2).txt))
            binding.radioButtonOption3.setText(mcqList.get(mcqPosition).options.get(2).txt);
        if (mcqList.get(mcqPosition).options.get(2).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(2).img))
            loadImage(binding.imageViewOption3, mcqList.get(mcqPosition).options.get(2).img);
        else
            binding.imageViewOption3.setVisibility(View.GONE);

        if (mcqList.get(mcqPosition).options.get(3).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(3).txt))
            binding.radioButtonOption4.setText(mcqList.get(mcqPosition).options.get(3).txt);
        if (mcqList.get(mcqPosition).options.get(3).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(3).img))
            loadImage(binding.imageViewOption4, mcqList.get(mcqPosition).options.get(3).img);
        else
            binding.imageViewOption4.setVisibility(View.GONE);
    }

    private void loadImage(ImageView imageView, String imagePath) {
        imageView.setVisibility(View.VISIBLE);
        AppUtilities.loadImage(this, imageView, BASE_URL2 + imagePath);
    }

    private void enableRadioButtons() {
        binding.radioGroupAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                clearSelection(false);
                View radioButton = binding.radioGroupAnswer.findViewById(checkedId);
                int index = binding.radioGroupAnswer.indexOfChild(radioButton);
                switch (index) {
                    case 0:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 1);
                        break;

                    case 2:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 2);
                        break;

                    case 4:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 3);
                        break;

                    case 6:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 4);
                        break;
                }
            }
        });
    }

    private void disableRadioButtons() {
        binding.radioGroupAnswer.setOnCheckedChangeListener(null);
        binding.radioGroupAnswer.clearCheck();
        clearSelection(true);
    }

    private void clearSelection(boolean isNewQuestion) {
        for (int i = 0; i < binding.radioGroupAnswer.getChildCount(); i++) {
            View view = binding.radioGroupAnswer.getChildAt(i);
            if (view instanceof RadioButton) {
                ((RadioButton) view).setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                ((RadioButton) view).setTextColor(Color.BLACK);
            } else if (view instanceof ImageView) {
                ((ImageView) view).setBackgroundColor(Color.WHITE);
            }

            if (isNewQuestion) {
                if (view instanceof TextView) {
                    ((TextView) view).setText("");
                }
                if (view instanceof ImageView) {
                    ((ImageView) view).setVisibility(View.GONE);
                }
            }
        }

        binding.cardViewAnswer.setVisibility(View.GONE);
    }

    private void showAnswerStatus(int position, boolean isRight) {
        if (isRight) { // Right Answer
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorGreen)));
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setTextColor(getColor(R.color.colorGreen));
            ((ImageView) binding.radioGroupAnswer.getChildAt(position + 1)).setBackgroundColor(getColor(R.color.colorGreen));
        } else { // Wrong Answer
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorRed)));
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setTextColor(getColor(R.color.colorRed));
            ((ImageView) binding.radioGroupAnswer.getChildAt(position + 1)).setBackgroundColor(getColor(R.color.colorRed));

            // Show Correct Answer
            binding.cardViewAnswer.setVisibility(View.VISIBLE);
            if (mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).txt)) {
                binding.textViewAnswer.setText(mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).txt);
                binding.textViewAnswer.setVisibility(View.VISIBLE);
            } else
                binding.textViewAnswer.setVisibility(View.GONE);

            if (mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).img))
                loadImage(binding.imageViewAnswer, mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).img);
            else
                binding.imageViewAnswer.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.scrollView.smoothScrollTo(0, (int) binding.cardViewAnswer.getY());
                }
            }, 300);
        }
    }

    private void getQuestionList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();

            MCQListRequest mcqListRequest = new MCQListRequest();
            mcqListRequest.bookId = mSessionManager.getSession(BOOK_ID);
            mcqListRequest.chapter = mSessionManager.getSession(CHAPTER);

            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getMCQList(mcqListRequest),
                    UrlConstants.URL_MCQ_LIST);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        MCQListResponse mcqListResponse = (MCQListResponse) response.body();
        if (mcqListResponse != null && mcqListResponse.retCode && mcqListResponse.returnData.size() > 0) {
            mcqList = (ArrayList<ReturnData>) mcqListResponse.returnData;
            if (mcqList.size() > 0) {
                setQuestionList(mcqPosition);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_mcq));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_mcq));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}