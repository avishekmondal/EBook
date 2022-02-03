package com.app.ebook.ui.activity;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.AppUtilities.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityResetPasswordBinding;
import com.app.ebook.models.ResetPasswordRequest;
import com.app.ebook.models.ResetPasswordResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;

import retrofit2.Call;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity implements RetrofitListener {

    private ActivityResetPasswordBinding binding;
    private RetroClient retroClient;

    public static final String RESET_PASSWORD_REQUEST = "ResetPasswordRequestExtra";

    private ResetPasswordRequest resetPasswordRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        if (getIntent() != null && getIntent().getSerializableExtra(RESET_PASSWORD_REQUEST) != null) {
            resetPasswordRequest = (ResetPasswordRequest) getIntent().getSerializableExtra(RESET_PASSWORD_REQUEST);

            Log.v("aa", resetPasswordRequest.email);
        } else {
            showToast(this, getString(R.string.something_went_wrong));
        }
    }

    public void onClickReset(View view) {
        if (isAllFieldsValid()) {
            if (AppUtilities.getInstance(this).isOnline()) {
                mProgressDialog.showProgressDialog();
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).resetPassword(resetPasswordRequest),
                        UrlConstants.URL_RESET_PASSWORD);
            } else {
                showSnackBar(binding.rootLayout, "No Internet!");
            }
        }
    }

    private boolean isAllFieldsValid() {
        boolean isValid = true;

        if (AppUtilities.getText(binding.editTextPassword).isEmpty()) {
            binding.editTextPassword.setError("Please enter password");
            isValid = false;
        }
        if (AppUtilities.getText(binding.editTextConfirmPassword).isEmpty()) {
            binding.editTextConfirmPassword.setError("Please Confirm password");
            isValid = false;
        } else if (!AppUtilities.getText(binding.editTextConfirmPassword).equals(AppUtilities.getText(binding.editTextPassword))) {
            binding.editTextConfirmPassword.setError("Password and confirm password should be same");
            isValid = false;
        } else {
            resetPasswordRequest.password = AppUtilities.getText(binding.editTextPassword);
        }

        return isValid;
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        if (method_name.equals(UrlConstants.URL_RESET_PASSWORD)) {
            ResetPasswordResponse resetPasswordResponse = (ResetPasswordResponse) response.body();
            if (resetPasswordResponse != null) {
                showToast(this, resetPasswordResponse.returnData);
                if (resetPasswordResponse.retCode) {
                    onBackPressed();
                }
            } else {
                showSnackBar(binding.rootLayout, resetPasswordResponse.returnData);
            }
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}