package com.app.ebook.ui.activity;

import static com.app.ebook.util.AppUtilities.showSnackBar;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityForgotPasswordBinding;
import com.app.ebook.models.ForgotPasswordRequest;
import com.app.ebook.models.ForgotPasswordResponse;
import com.app.ebook.models.VerifyOTPRequest;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;

import retrofit2.Call;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity implements RetrofitListener {

    private ActivityForgotPasswordBinding binding;
    private RetroClient retroClient;

    private ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
    private VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

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
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickSend(View view) {
        if (isAllFieldsValid()) {
            if (AppUtilities.getInstance(this).isOnline()) {
                mProgressDialog.showProgressDialog();
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).forgotPassword(forgotPasswordRequest), UrlConstants.URL_FORGOT_PASSWORD);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_connection));
            }
        }
    }

    private boolean isAllFieldsValid() {
        if (AppUtilities.getText(binding.editTextEmail).isEmpty() ||
                !AppUtilities.isValidEmail(AppUtilities.getText(binding.editTextEmail))) {
            binding.editTextEmail.setError("Please enter a valid email");
            return false;
        } else {
            forgotPasswordRequest.email = AppUtilities.getText(binding.editTextEmail);
        }
        return true;
    }

    private void openVerifyOTPActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VerifyOTPActivity.VERIFY_OTP_REQUEST_EXTRA, verifyOTPRequest);
        startTargetActivity(VerifyOTPActivity.class, bundle);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        switch (method_name) {
            case UrlConstants.URL_FORGOT_PASSWORD:
                ForgotPasswordResponse forgotPasswordResponse = (ForgotPasswordResponse) response.body();
                if (forgotPasswordResponse != null) {
                    if (forgotPasswordResponse.retCode) {
                        verifyOTPRequest.email = forgotPasswordRequest.email;
                        verifyOTPRequest.otp = forgotPasswordResponse.returnOtp;
                        openVerifyOTPActivity();
                    } else {
                        showSnackBar(binding.rootLayout, forgotPasswordResponse.returnData);
                    }
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
}