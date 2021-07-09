package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityOrderSummaryBinding;
import com.app.ebook.models.book_subscription.AddToSubscriptionRequest;
import com.app.ebook.models.book_subscription.AddToSubscriptionResponse;
import com.app.ebook.models.cart.CartListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.adapter.OrderAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.URL_ADD_SUBSCRIPTION_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;

public class OrderSummaryActivity extends BaseActivity implements RetrofitListener {

    public static final String ORDER_SUMMARY_EXTRA = "OrderSummaryExtra";

    private ActivityOrderSummaryBinding binding;
    private RetroClient retroClient;

    private List<CartListResponse.ReturnDataBean> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_summary);

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

        orderList = (List<CartListResponse.ReturnDataBean>) getIntent().getSerializableExtra(ORDER_SUMMARY_EXTRA);
        OrderAdapter orderAdapter = new OrderAdapter(this, orderList);
        binding.recyclerViewOrderList.setAdapter(orderAdapter);
        binding.textViewTotalItem.setText("" + orderList.size());

        int totalPrice = 0;
        for (CartListResponse.ReturnDataBean returnDataBean : orderList) {
            totalPrice += Integer.parseInt(returnDataBean.price);
        }
        binding.textViewSubTotal.setText("Rs. " + totalPrice);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickProceedToPay(View view) {
        addToSubscription();
    }

    private void addToSubscription() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            AddToSubscriptionRequest addToSubscriptionRequest = new AddToSubscriptionRequest();
            addToSubscriptionRequest.userId = mUser.username;
            addToSubscriptionRequest.productIds = new ArrayList<>();
            for (CartListResponse.ReturnDataBean returnDataBean : orderList) {
                Log.v("aa", returnDataBean.productId);
                addToSubscriptionRequest.productIds.add(returnDataBean.productId);
            }

            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).addSubscriptionList(addToSubscriptionRequest),
                    URL_ADD_SUBSCRIPTION_LIST);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        AddToSubscriptionResponse addToSubscriptionResponse = (AddToSubscriptionResponse) response.body();
        if (addToSubscriptionResponse != null) {
            if (addToSubscriptionResponse.retCode) {
                AppUtilities.showToast(this, "Products Subscribed");
                startTargetActivityNewTask(MainActivity.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                Toast.makeText(this, addToSubscriptionResponse.message, Toast.LENGTH_LONG).show();
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }

}
