package com.app.ebook.ui.activity;

import static com.app.ebook.util.AppUtilities.showSnackBar;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivitySignupBinding;
import com.app.ebook.models.CheckDuplicateUserRequest;
import com.app.ebook.models.CheckDuplicateUserResponse;
import com.app.ebook.models.RegistrationRequest;
import com.app.ebook.models.VerifyOTPRequest;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements RetrofitListener {

    private ActivitySignupBinding binding;
    private RetroClient retroClient;

    /*ArrayList<String> boardList = new ArrayList<>();
    ArrayList<String> classList = new ArrayList<>();
    private ListPopupWindow popupWindow = null;

    private String sClass = "", sBoard = "";*/

    private final CheckDuplicateUserRequest checkDuplicateUserRequest = new CheckDuplicateUserRequest();
    private final VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();
    private final RegistrationRequest registrationRequest = new RegistrationRequest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        init();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        /*for (BoardListResponse boardListResponse : mBoardList)
            boardList.add(boardListResponse.boardFullName + " (" + boardListResponse.boardShortName + ")");

        for (ClassListResponse classListResponse : mClassList)
            classList.add(classListResponse.className);*/
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

    /*public void onClickSelectClass(View view) {
        binding.editTextClass.setError(null);
        popupWindow = AppUtilities.showAnchoredPopup(this,
                classList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                sClass = classList.get(position);
                binding.editTextClass.setText("Class " + sClass);
            }
        });
        popupWindow.show();
    }

    public void onClickSelectBoard(View view) {
        binding.editTextBoard.setError(null);
        popupWindow = AppUtilities.showAnchoredPopup(this,
                boardList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                sBoard = mBoardList.get(position).boardShortName;
                binding.editTextBoard.setText(sBoard);
            }
        });
        popupWindow.show();
    }*/

    public void onClickSignUp(View view) {
        if (isAllFieldsValid()) {
            makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).checkDuplicateUser(checkDuplicateUserRequest),
                    UrlConstants.URL_CHECK_DUPLICATE_USER);
        }
    }

    public void onClickSignIn(View view) {
        onBackPressed();
    }

    private boolean isAllFieldsValid() {
        boolean isValid = true;

        if (AppUtilities.getText(binding.editTextName).isEmpty()) {
            binding.editTextName.setError("Please enter a valid name");
            isValid = false;
        } else {
            //registrationRequest.fname = AppUtilities.getName(this, AppUtilities.getText(binding.editTextName), 0);
            //registrationRequest.lname = AppUtilities.getName(this, AppUtilities.getText(binding.editTextName), 1);
            registrationRequest.name = checkDuplicateUserRequest.name = AppUtilities.getText(binding.editTextName);
            registrationRequest.username = checkDuplicateUserRequest.username = registrationRequest.name.substring(0, 1).toLowerCase() + System.currentTimeMillis();
        }

        if (AppUtilities.getText(binding.editTextEmail).isEmpty() ||
                !AppUtilities.isValidEmail(AppUtilities.getText(binding.editTextEmail))) {
            binding.editTextEmail.setError("Please enter a valid email");
            isValid = false;
        } else {
            registrationRequest.email = checkDuplicateUserRequest.email = AppUtilities.getText(binding.editTextEmail);
        }

        if (AppUtilities.getText(binding.editTextMobile).isEmpty() ||
                !AppUtilities.isValidPhoneNo(AppUtilities.getText(binding.editTextMobile))) {
            binding.editTextMobile.setError("Please enter a valid mobile no");
            isValid = false;
        } else {
            registrationRequest.mobile = checkDuplicateUserRequest.mobile = AppUtilities.getText(binding.editTextMobile);
        }

        /*if (sClass.isEmpty()) {
            binding.editTextClass.setError("Please select your class");
            return false;
        } else {
            registrationRequest.sClass = sClass;
        }

        if (sBoard.isEmpty()) {
            binding.editTextBoard.setError("Please select your board");
            return false;
        } else {
            registrationRequest.boardName = sBoard;
        }

        if (AppUtilities.getText(binding.editTextSchool).isEmpty()) {
            binding.editTextSchool.setError("Please enter your school name");
            return false;
        } else {
            registrationRequest.instituteName = AppUtilities.getText(binding.editTextSchool);
        }*/

        if (AppUtilities.getText(binding.editTextPassword).isEmpty()) {
            binding.editTextPassword.setError("Please enter password");
            isValid = false;
        }

        if (AppUtilities.getText(binding.editTextConfirmPassword).isEmpty()) {
            binding.editTextConfirmPassword.setError("Please confirm password");
            isValid = false;
        } else if (!AppUtilities.getText(binding.editTextConfirmPassword).equals(AppUtilities.getText(binding.editTextPassword))) {
            binding.editTextConfirmPassword.setError("Password and confirm password should be same");
            isValid = false;
        } else {
            registrationRequest.password = checkDuplicateUserRequest.password = AppUtilities.getText(binding.editTextPassword);
        }
        return isValid;
    }

    private void openVerifyOTPActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VerifyOTPActivity.VERIFY_OTP_REQUEST_EXTRA, verifyOTPRequest);
        bundle.putSerializable(VerifyOTPActivity.REGISTRATION_REQUEST_EXTRA, registrationRequest);
        startTargetActivity(VerifyOTPActivity.class, bundle);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_CHECK_DUPLICATE_USER:
                mProgressDialog.hideProgressDialog();
                CheckDuplicateUserResponse checkDuplicateUserResponse = (CheckDuplicateUserResponse) response.body();
                if (checkDuplicateUserResponse != null) {
                    if (checkDuplicateUserResponse.retCode) {
                        verifyOTPRequest.email = checkDuplicateUserRequest.email;
                        verifyOTPRequest.otp = checkDuplicateUserResponse.returnOtp;
                        openVerifyOTPActivity();
                    } else {
                        if (checkDuplicateUserResponse.details.name != null)
                            binding.editTextName.setError(checkDuplicateUserResponse.details.name.get(0));
                        if (checkDuplicateUserResponse.details.email != null)
                            binding.editTextEmail.setError(checkDuplicateUserResponse.details.email.get(0));
                        if (checkDuplicateUserResponse.details.mobile != null)
                            binding.editTextMobile.setError(checkDuplicateUserResponse.details.mobile.get(0));
                        if (checkDuplicateUserResponse.details.password != null)
                            binding.editTextPassword.setError(checkDuplicateUserResponse.details.password.get(0));
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
        showSnackBar(binding.rootLayout, errorMessage);
    }

}
