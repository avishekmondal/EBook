package com.app.ebook.ui.activity;

import static com.app.ebook.network.UrlConstants.URL_ADD_SUBSCRIPTION_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.MERCHANT_ID;
import static com.app.ebook.util.Constants.MERCHANT_KEY;
import static com.app.ebook.util.Constants.MERCHANT_SALT;

import android.content.Intent;
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
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OrderSummaryActivity extends BaseActivity implements RetrofitListener {
    private final String TAG = "OrderSummaryActivity";
    public static final String ORDER_SUMMARY_EXTRA = "OrderSummaryExtra";

    private ActivityOrderSummaryBinding binding;
    private RetroClient retroClient;

    private List<CartListResponse.ReturnDataBean> orderList = new ArrayList<>();
    int totalPrice = 0;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

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

        for (CartListResponse.ReturnDataBean returnDataBean : orderList) {
            totalPrice += Integer.parseInt(returnDataBean.price);
        }
        binding.textViewSubTotal.setText("Rs. " + totalPrice);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickProceedToPay(View view) {
        //addToSubscription();
        makePayment();
    }

    private void makePayment() {
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount(String.valueOf(totalPrice))
                .setTxnId(String.valueOf(System.currentTimeMillis()))
                .setPhone(mUser.mobile)
                .setProductName(getString(R.string.app_name))
                .setFirstName(mUser.name)
                .setEmail(mUser.email)
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(true)
                .setKey(MERCHANT_KEY)
                .setMerchantId(MERCHANT_ID);

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            //    generateHashFromServer(mPaymentParams);

            /*
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment(mPaymentParams);

            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, OrderSummaryActivity.this, R.style.AppTheme_NoActionBar, false);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        stringBuilder.append(MERCHANT_SALT);

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d(TAG, "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    Log.v("test", "success");
                    addToSubscription();
                } else {
                    Log.v("test", "failure");
                }

                /*
                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();*/

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
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
