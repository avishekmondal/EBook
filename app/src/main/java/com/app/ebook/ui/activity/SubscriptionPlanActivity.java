package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivitySubscriptionPlanBinding;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.models.book_subscription.SubscriptionPlanListRequest;
import com.app.ebook.models.book_subscription.SubscriptionPlanListResponse;
import com.app.ebook.models.cart.CartListResponse;
import com.app.ebook.models.cart.UpdateCartRequest;
import com.app.ebook.models.cart.UpdateCartResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.SubscriptionPlanAdapter;
import com.app.ebook.util.AppUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.URL_ADD_CART;
import static com.app.ebook.network.UrlConstants.URL_SUBSCRIPTION_PLAN_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.AppUtilities.showToast;

public class SubscriptionPlanActivity extends BaseActivity implements RetrofitListener, SubscriptionPlanAdapter.SubscriptionPlanListItemClickListener {

    public static final String BOOK_DETAILS_EXTRA = "BookDetailsExtra";

    private ActivitySubscriptionPlanBinding binding;
    private RetroClient retroClient;

    private BookListResponse.ReturnResponseBean bookDataBean = new BookListResponse.ReturnResponseBean();
    private SubscriptionPlanListResponse.ReturnDataBean subscriptionDataBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_plan);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        if (getIntent().hasExtra(BOOK_DETAILS_EXTRA)) {
            bookDataBean = (BookListResponse.ReturnResponseBean) getIntent().getSerializableExtra(BOOK_DETAILS_EXTRA);
            binding.textViewBookName.setText(bookDataBean.bookName);

            getSubscriptionPlanList();
        } else {
            showToast(this, getResources().getString(R.string.something_went_wrong));
            onBackPressed();
        }
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickCart(View view) {
        startTargetActivity(CartActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onClickSubscribe(View view) {
        if (subscriptionDataBean != null) {
            List<CartListResponse.ReturnDataBean> cartList = new ArrayList<>();
            CartListResponse.ReturnDataBean returnDataBean = new CartListResponse.ReturnDataBean();
            returnDataBean.bookName = bookDataBean.bookName;
            returnDataBean.coverPhoto = bookDataBean.coverPhoto;
            returnDataBean.productId = subscriptionDataBean.productId;
            returnDataBean.bookId = subscriptionDataBean.bookId;
            returnDataBean.plan = subscriptionDataBean.plan;
            returnDataBean.price = subscriptionDataBean.price;
            returnDataBean.duration = subscriptionDataBean.duration;
            cartList.add(returnDataBean);

            Bundle bundle = new Bundle();
            bundle.putSerializable(OrderSummaryActivity.ORDER_SUMMARY_EXTRA, (Serializable) cartList);
            startTargetActivity(OrderSummaryActivity.class, bundle);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickAddToCart(View view) {
        if (subscriptionDataBean != null) {
            addToCart();
        }
    }

    private void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getSubscriptionPlanList() {
        SubscriptionPlanListRequest subscriptionPlanListRequest = new SubscriptionPlanListRequest();
        subscriptionPlanListRequest.bookId = bookDataBean.bookId;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getSubscriptionPlanList(subscriptionPlanListRequest),
                URL_SUBSCRIPTION_PLAN_LIST);
    }

    private void addToCart() {
        UpdateCartRequest updateCartRequest = new UpdateCartRequest();
        updateCartRequest.userId = mUser.username;
        updateCartRequest.productId = subscriptionDataBean.productId;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).addToCart(updateCartRequest),
                URL_ADD_CART);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        switch (method_name) {
            case URL_SUBSCRIPTION_PLAN_LIST:
                SubscriptionPlanListResponse subscriptionPlanListResponse = (SubscriptionPlanListResponse) response.body();
                if (subscriptionPlanListResponse != null && subscriptionPlanListResponse.retCode) {
                    ArrayList<SubscriptionPlanListResponse.ReturnDataBean> subscriptionPlanList = (ArrayList<SubscriptionPlanListResponse.ReturnDataBean>) subscriptionPlanListResponse.returnData;
                    if (subscriptionPlanList.size() > 0) {
                        SubscriptionPlanAdapter subscriptionPlanAdapter = new SubscriptionPlanAdapter(this,
                                subscriptionPlanList, this);
                        binding.recyclerViewSubscriptionPlanList.setAdapter(subscriptionPlanAdapter);
                    } else {
                        showSnackBar(binding.rootLayout, getString(R.string.no_subscription_plan));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.no_subscription_plan));
                }
                break;
            case UrlConstants.URL_ADD_CART:
                UpdateCartResponse updateCartResponse = (UpdateCartResponse) response.body();
                if (updateCartResponse != null) {
                    showSnackBar(binding.rootLayout, updateCartResponse.message);
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                }
                break;
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }

    @Override
    public void onSubscriptionPlanListItemClick(SubscriptionPlanListResponse.ReturnDataBean returnDataBean) {
        subscriptionDataBean = returnDataBean;
        binding.buttonSubscribe.setAlpha(1.0f);
        binding.buttonAddToCart.setAlpha(1.0f);
    }
}
