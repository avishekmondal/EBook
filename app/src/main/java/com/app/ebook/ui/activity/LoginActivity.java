package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityLoginBinding;
import com.app.ebook.models.LoginRequest;
import com.app.ebook.models.UserDetailsResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.Constants;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;

public class LoginActivity extends BaseActivity implements RetrofitListener {

    private ActivityLoginBinding binding;
    private RetroClient retroClient;
    private final LoginRequest loginRequest = new LoginRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        retroClient = new RetroClient(this, this);

        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.editTextEmail.setError(null);
                if (isEmailOrPhoneNoValid()) {
                    binding.editTextEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email, 0, R.drawable.ic_action_check, 0);
                } else {
                    binding.editTextEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email, 0, 0, 0);
                }
            }
        });
    }

    public void onClickLogin(View view) {
        if (isAllFieldsValid()) {
            if (AppUtilities.getInstance(this).isOnline()) {
                mProgressDialog.showProgressDialog();
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).login(loginRequest),
                        UrlConstants.URL_LOGIN);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_connection));
            }
        }
    }

    public void onClickForgotPassword(View view) {
        startTargetActivity(ForgotPasswordActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onClickSignUp(View view) {
        startTargetActivity(SignUpActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private boolean isEmailOrPhoneNoValid() {
        return binding.editTextEmail.getText() != null && (AppUtilities.isValidEmail(binding.editTextEmail.getText().toString()) || AppUtilities.isValidPhoneNo(binding.editTextEmail.getText().toString()));
    }

    private boolean isAllFieldsValid() {
        boolean isValid = true;

        if (!isEmailOrPhoneNoValid()) {
            binding.editTextEmail.setError("Please enter a valid email or phone no");
            isValid = false;
        } else {
            if (AppUtilities.isValidEmail(binding.editTextEmail.getText().toString())) {
                loginRequest.email = AppUtilities.getText(binding.editTextEmail);
            } else if (AppUtilities.isValidPhoneNo(binding.editTextEmail.getText().toString())) {
                loginRequest.mobile = AppUtilities.getText(binding.editTextEmail);
            }
        }

        if (binding.editTextPassword.getText() == null ||
                binding.editTextPassword.getText().toString().isEmpty()) {
            binding.editTextPassword.setError("Please enter your password");
            isValid = false;
        } else {
            loginRequest.password = AppUtilities.getText(binding.editTextPassword);
        }

        return isValid;
    }

    private void openMainActivity() {
        startTargetActivity(MainActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        switch (method_name) {
            case UrlConstants.URL_LOGIN:
                UserDetailsResponse userDetailsResponse = (UserDetailsResponse) response.body();
                if (userDetailsResponse != null) {
                    mSessionManager.setSession(Constants.IS_LOGGEDIN, true);
                    mSessionManager.setSession(Constants.CURRENT_USER, new Gson().toJson(userDetailsResponse, UserDetailsResponse.class));
                    mSessionManager.setSession(Constants.USER_TOKEN, userDetailsResponse.token);
                    openMainActivity();
                } else
                    showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                break;
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, errorMessage);
    }

}