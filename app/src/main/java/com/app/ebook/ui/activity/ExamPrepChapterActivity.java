package com.app.ebook.ui.activity;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.IS_SUBSCRIBED;
import static com.app.ebook.util.Constants.KEY;
import static com.app.ebook.util.Constants.MCQ_CATEGORY;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityExamPrepChapterQuestionListBinding;
import com.app.ebook.models.exam_prep_chapters.ExamPrepChapterListRequest;
import com.app.ebook.models.exam_prep_chapters.ExamPrepChapterListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.ExamPrepChapterAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ExamPrepChapterActivity extends BaseActivity implements RetrofitListener {

    private ActivityExamPrepChapterQuestionListBinding binding;
    private RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_prep_chapter_question_list);

        init();
        getChapterList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        binding.textViewTitle.setText(mBookDetails.bookName);
        if (!mSessionManager.getBooleanSession(IS_SUBSCRIBED)) {
            binding.layoutSubscribeNow.setVisibility(View.VISIBLE);
        } else {
            binding.layoutSubscribeNow.setVisibility(View.GONE);
        }
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getChapterList() {
        ExamPrepChapterListRequest examPrepChapterListRequest = new ExamPrepChapterListRequest();
        examPrepChapterListRequest.bookId = mBookDetails.bookId;

        if (mSessionManager.getSession(KEY).equalsIgnoreCase(getString(R.string.menu_mcq))) {
            examPrepChapterListRequest.category = mSessionManager.getSession(MCQ_CATEGORY);
            if (!mSessionManager.getBooleanSession(IS_SUBSCRIBED)) {
                makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getPreviewMcqChapterList(examPrepChapterListRequest),
                        UrlConstants.URL_PREVIEW_MCQ_CHAPTER_LIST);
            } else {
                makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getMcqChapterList(examPrepChapterListRequest),
                        UrlConstants.URL_MCQ_CHAPTER_LIST);
            }
        } else {
            if (!mSessionManager.getBooleanSession(IS_SUBSCRIBED)) {
                makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getPreviewSubjectiveChapterList(examPrepChapterListRequest),
                        UrlConstants.URL_PREVIEW_SUBJECTIVE_CHAPTER_LIST);
            } else {
                makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getSubjectiveChapterList(examPrepChapterListRequest),
                        UrlConstants.URL_SUBJECTIVE_CHAPTER_LIST);
            }
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        ExamPrepChapterListResponse examPrepChapterListResponse = (ExamPrepChapterListResponse) response.body();
        if (examPrepChapterListResponse != null && examPrepChapterListResponse.retCode) {
            List<String> chapterList = examPrepChapterListResponse.returnData;
            if (chapterList.size() > 0) {
                ExamPrepChapterAdapter adapter = new ExamPrepChapterAdapter(ExamPrepChapterActivity.this,
                        chapterList);
                binding.recyclerViewChapterQuestionList.setAdapter(adapter);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_chapters));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_chapters));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}