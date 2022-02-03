package com.app.ebook.ui.activity;

import static com.app.ebook.network.UrlConstants.URL_FAQ_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityFaqCategoryBinding;
import com.app.ebook.models.FAQResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.adapter.FAQCategoryAdapter;
import com.app.ebook.util.AppUtilities;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class FAQCategoryActivity extends BaseActivity implements RetrofitListener, FAQCategoryAdapter.FAQCategoryItemClickListener {

    private ActivityFaqCategoryBinding binding;

    private RetroClient retroClient;

    private FAQCategoryAdapter faqCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq_category);

        init();
        getFAQList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        faqCategoryAdapter = new FAQCategoryAdapter(this, this);
        binding.recyclerViewFaqCategoryList.setAdapter(faqCategoryAdapter);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    private void getFAQList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getFAQList(),
                    URL_FAQ_LIST);
        }
    }

    @Override
    public void onFAQCategoryItemClick(FAQResponse.ReturnResponse returnResponse) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FAQActivity.FAQ_EXTRA, (Serializable) returnResponse);
        startTargetActivity(FAQActivity.class, bundle);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        FAQResponse faqResponse = (FAQResponse) response.body();
        if (faqResponse != null && faqResponse.retCode) {
            ArrayList<FAQResponse.ReturnResponse> faqList = (ArrayList<FAQResponse.ReturnResponse>) faqResponse.returnResponse;
            if (faqList.size() > 0) {
                faqCategoryAdapter.setData(faqList);
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_faqs));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}
