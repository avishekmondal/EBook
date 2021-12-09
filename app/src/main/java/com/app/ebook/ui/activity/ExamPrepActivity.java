package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityExamPrepBinding;
import com.app.ebook.models.exam_prep_chapters.MCQCategoryListRequest;
import com.app.ebook.models.exam_prep_chapters.MCQCategoryListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.MCQCategoryListAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.KEY;

public class ExamPrepActivity extends BaseActivity implements RetrofitListener {

    private ActivityExamPrepBinding binding;
    private RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_prep);

        init();
        getMCQCategoryList();
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
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickSubjective(View view) {
        mSessionManager.setSession(KEY, getString(R.string.menu_subjective));
        startTargetActivity(ExamPrepChapterActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void getMCQCategoryList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();

            MCQCategoryListRequest mcqCategoryListRequest = new MCQCategoryListRequest();
            mcqCategoryListRequest.bookId = mBookDetails.bookId;

            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getMCQCategoryList(mcqCategoryListRequest),
                    UrlConstants.URL_MCQ_CATEGORY_LIST);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        MCQCategoryListResponse mcqCategoryListResponse = (MCQCategoryListResponse) response.body();
        if (mcqCategoryListResponse != null && mcqCategoryListResponse.retCode && mcqCategoryListResponse.returnResponse.size() > 0) {
            List<String> mcqCategoryList = mcqCategoryListResponse.returnResponse;
            MCQCategoryListAdapter mcqCategoryListAdapter = new MCQCategoryListAdapter(this, mcqCategoryList);
            binding.recyclerViewMCQCategoryList.setAdapter(mcqCategoryListAdapter);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_mcq));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
    }
}