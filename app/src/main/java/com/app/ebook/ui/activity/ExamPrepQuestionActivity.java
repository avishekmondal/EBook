package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityExamPrepChapterQuestionListBinding;
import com.app.ebook.models.subjective.ReturnData;
import com.app.ebook.models.subjective.SubjectiveListRequest;
import com.app.ebook.models.subjective.SubjectiveListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.ExamPrepQuestionAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.CHAPTER;
import static com.app.ebook.util.Constants.IS_SUBSCRIBED;

public class ExamPrepQuestionActivity extends BaseActivity implements RetrofitListener {

    private ActivityExamPrepChapterQuestionListBinding binding;
    private RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_prep_chapter_question_list);

        init();
        getSubjectiveList();
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

    private void getSubjectiveList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            SubjectiveListRequest subjectiveListRequest = new SubjectiveListRequest();
            subjectiveListRequest.bookId = mBookDetails.bookId;
            subjectiveListRequest.chapter = mSessionManager.getSession(CHAPTER);

            if (!mSessionManager.getBooleanSession(IS_SUBSCRIBED)) {
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getPreviewSubjectiveList(subjectiveListRequest),
                        UrlConstants.URL_PREVIEW_SUBJECTIVE_LIST);
            } else {
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getSubjectiveList(subjectiveListRequest),
                        UrlConstants.URL_SUBJECTIVE_LIST);
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        SubjectiveListResponse subjectiveQuestionResponse = (SubjectiveListResponse) response.body();
        if (subjectiveQuestionResponse != null && subjectiveQuestionResponse.retCode) {
            List<ReturnData> questionList = subjectiveQuestionResponse.returnData;
            if (questionList.size() > 0) {
                ExamPrepQuestionAdapter adapter = new ExamPrepQuestionAdapter(ExamPrepQuestionActivity.this,
                        questionList);
                binding.recyclerViewChapterQuestionList.setAdapter(adapter);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_question));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_question));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}