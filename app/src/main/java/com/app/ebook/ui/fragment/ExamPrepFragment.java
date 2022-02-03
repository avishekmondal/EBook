package com.app.ebook.ui.fragment;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentExamPrepBinding;
import com.app.ebook.models.exam_prep_chapters.MCQCategoryListRequest;
import com.app.ebook.models.exam_prep_chapters.MCQCategoryListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.activity.ExamPrepChapterActivity;
import com.app.ebook.ui.adapter.MCQCategoryListAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ExamPrepFragment extends BaseFragment implements RetrofitListener {

    private FragmentExamPrepBinding binding;
    private RetroClient retroClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exam_prep, container, false);

        init();
        initClickListener();
        getMCQCategoryList();

        return binding.getRoot();
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);
    }

    private void initClickListener() {
        binding.layoutSubjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionManager.setSession(KEY, getString(R.string.menu_subjective));
                startTargetActivity(ExamPrepChapterActivity.class);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void getMCQCategoryList() {
        if (AppUtilities.getInstance(getActivity()).isOnline()) {
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
            MCQCategoryListAdapter mcqCategoryListAdapter = new MCQCategoryListAdapter(getActivity(), mcqCategoryList);
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