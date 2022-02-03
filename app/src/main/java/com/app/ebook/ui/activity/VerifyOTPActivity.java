package com.app.ebook.ui.activity;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.AppUtilities.showToast;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityVerifyOTPBinding;
import com.app.ebook.models.RegistrationRequest;
import com.app.ebook.models.RegistrationResponse;
import com.app.ebook.models.ResendOTPRequest;
import com.app.ebook.models.ResendOTPResponse;
import com.app.ebook.models.ResetPasswordRequest;
import com.app.ebook.models.UpdateUserDetailsRequest;
import com.app.ebook.models.UpdateUserDetailsResponse;
import com.app.ebook.models.VerifyOTPRequest;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.Constants;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

public class VerifyOTPActivity extends BaseActivity implements RetrofitListener {

    public static final String VERIFY_OTP_REQUEST_EXTRA = "VerifyOTPRequestExtra";
    public static final String REGISTRATION_REQUEST_EXTRA = "RegistrationRequestExtra";
    public static final String UPDATE_USER_DETAILS_REQUEST_EXTRA = "UpdateUserDetailsRequestExtra";

    private ActivityVerifyOTPBinding binding;
    private RetroClient retroClient;

    private VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();
    private RegistrationRequest registrationRequest = new RegistrationRequest();
    private UpdateUserDetailsRequest updateUserDetailsRequest = new UpdateUserDetailsRequest();

    private boolean isRegistration = false, isUpdateProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_o_t_p);

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

        if (getIntent().hasExtra(VERIFY_OTP_REQUEST_EXTRA))
            verifyOTPRequest = (VerifyOTPRequest) getIntent().getSerializableExtra(VERIFY_OTP_REQUEST_EXTRA);

        if (getIntent().hasExtra(REGISTRATION_REQUEST_EXTRA)) {
            registrationRequest = (RegistrationRequest) getIntent().getSerializableExtra(REGISTRATION_REQUEST_EXTRA);
            isRegistration = true;
        }else if (getIntent().hasExtra(UPDATE_USER_DETAILS_REQUEST_EXTRA)) {
            updateUserDetailsRequest = (UpdateUserDetailsRequest) getIntent().getSerializableExtra(UPDATE_USER_DETAILS_REQUEST_EXTRA);
            isUpdateProfile = true;
        }

        binding.textViewVerificationText.setText("Enter OTP sent to your email and mobile");

        binding.otpView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                binding.textViewVerificationError.setVisibility(View.INVISIBLE);
                return false;
            }
        });
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickVerifyOTP(View view) {
        String otp = binding.otpView.getText().toString();
        if (otp.equals(verifyOTPRequest.otp)) {
            if (isRegistration) {
                makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).registration(registrationRequest),
                        UrlConstants.URL_REGISTRATION);
            } else if (isUpdateProfile) {
                makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).updateUserDetails(updateUserDetailsRequest),
                        UrlConstants.URL_UPDATE_USER_DETAILS);
            } else {
                openResetPasswordActivity();
            }
        } else {
            binding.textViewVerificationError.setVisibility(View.VISIBLE);
        }
    }

    public void onClickResendOTP(View view) {
        ResendOTPRequest resendOTPRequest = new ResendOTPRequest();
        resendOTPRequest.email = verifyOTPRequest.email;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).resendOTP(resendOTPRequest),
                UrlConstants.URL_RESEND_OTP);
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void openResetPasswordActivity() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.email = verifyOTPRequest.email;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ResetPasswordActivity.RESET_PASSWORD_REQUEST, resetPasswordRequest);
        startTargetActivity(ResetPasswordActivity.class, bundle);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        switch (method_name) {
            case UrlConstants.URL_RESEND_OTP:
                ResendOTPResponse resendOTPResponse = (ResendOTPResponse) response.body();
                if (resendOTPResponse != null) {
                    if (resendOTPResponse.retCode) {
                        verifyOTPRequest.otp = resendOTPResponse.returnOtp;
                        showSnackBar(binding.rootLayout, "OTP Sent.");
                    } else {
                        showSnackBar(binding.rootLayout, resendOTPResponse.returnData);
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                }
                break;
            case UrlConstants.URL_REGISTRATION:
                RegistrationResponse registrationResponse = (RegistrationResponse) response.body();
                if (registrationResponse != null) {
                    if (registrationResponse.retCode) {
                        showToast(this, registrationResponse.message);
                        mSessionManager.setSession(Constants.IS_LOGGEDIN, true);
                        mSessionManager.setSession(Constants.CURRENT_USER, new Gson().toJson(registrationResponse.returnData, RegistrationResponse.ReturnDataBean.class));
                        mSessionManager.setSession(Constants.USER_TOKEN, registrationResponse.returnData.token);
                        startTargetActivity(MainActivity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                }
                break;
            case UrlConstants.URL_UPDATE_USER_DETAILS:
                UpdateUserDetailsResponse updateUserDetailsResponse = (UpdateUserDetailsResponse) response.body();
                if (updateUserDetailsResponse != null) {
                    if (updateUserDetailsResponse.retCode) {
                        showToast(this, updateUserDetailsResponse.message);
                        mSessionManager.setSession(Constants.CURRENT_USER, new Gson().toJson(updateUserDetailsResponse.returnData, UpdateUserDetailsResponse.ReturnDataBean.class));
                        onBackPressed();
                    } else {
                        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
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