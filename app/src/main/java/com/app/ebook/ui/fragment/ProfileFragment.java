package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentProfileBinding;
import com.app.ebook.models.BoardListResponse;
import com.app.ebook.models.CheckDuplicateProfileRequest;
import com.app.ebook.models.CheckDuplicateProfileResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.models.UpdateUserDetailsRequest;
import com.app.ebook.models.VerifyOTPRequest;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.activity.VerifyOTPActivity;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;

public class ProfileFragment extends BaseFragment implements RetrofitListener {

    private FragmentProfileBinding binding;
    private RetroClient retroClient;

    ArrayList<String> classList = new ArrayList<>();
    ArrayList<String> boardList = new ArrayList<>();
    private ListPopupWindow popupWindow = null;

    private String sClass = "", sBoard = "";

    private List<BoardListResponse> boardListResponseList = new ArrayList<>();
    private List<ClassListResponse> classListResponseList = new ArrayList<>();
    private final CheckDuplicateProfileRequest checkDuplicateProfileRequest = new CheckDuplicateProfileRequest();
    private final VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();
    private final UpdateUserDetailsRequest updateUserDetailsRequest = new UpdateUserDetailsRequest();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        init();
        initClickListener();
        getBoardList();

        return binding.getRoot();
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);

        binding.layoutClass.setEnabled(false);
        binding.editTextName.setText(mUser.fullName);
        binding.textViewClass.setText("Class " + mUser.sClass);
        binding.editTextEmail.setText(mUser.email);
        binding.editTextMobile.setText(mUser.mobile);
        binding.editTextBoard.setText(mUser.boardName);
        binding.editTextSchool.setText(mUser.instituteName);
    }

    private void initClickListener() {
        binding.layoutClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                        classList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        sClass = classList.get(position);
                        binding.textViewClass.setText("Class " + sClass);
                    }
                });
                popupWindow.show();
            }
        });

        binding.editTextBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.editTextBoard.setError(null);
                popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                        boardList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        sBoard = boardListResponseList.get(position).boardShortName;
                        binding.editTextBoard.setText(sBoard);
                    }
                });
                popupWindow.show();
            }
        });

        binding.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //isEditable = true;
                binding.buttonEdit.setVisibility(View.GONE);
                binding.buttonSave.setVisibility(View.VISIBLE);

                binding.layoutClass.setEnabled(true);
                binding.imageViewDropDownClass.setVisibility(View.VISIBLE);
                binding.editTextEmail.setEnabled(true);
                binding.editTextEmail.requestFocus();
                binding.editTextMobile.setEnabled(true);
                binding.editTextBoard.setEnabled(true);
                binding.editTextSchool.setEnabled(true);
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllFieldsValid()) {
                    makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).checkDuplicateProfile(checkDuplicateProfileRequest), UrlConstants.URL_CHECK_DUPLICATE_PROFILE);
                }
            }
        });
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(getActivity()).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private boolean isAllFieldsValid() {
        updateUserDetailsRequest.username =  checkDuplicateProfileRequest.username = mUser.username;

        if (AppUtilities.getText(binding.editTextEmail).isEmpty() ||
                !AppUtilities.isValidEmail(AppUtilities.getText(binding.editTextEmail))) {
            binding.editTextEmail.setError("Please enter a valid email");
            return false;
        } else {
            updateUserDetailsRequest.email = checkDuplicateProfileRequest.email = AppUtilities.getText(binding.editTextEmail);
        }

        if (AppUtilities.getText(binding.editTextMobile).isEmpty() ||
                !AppUtilities.isValidPhoneNo(AppUtilities.getText(binding.editTextMobile))) {
            binding.editTextMobile.setError("Please enter a valid mobile no");
            return false;
        } else {
            updateUserDetailsRequest.mobile = checkDuplicateProfileRequest.mobile = AppUtilities.getText(binding.editTextMobile);
        }

        if (AppUtilities.getText(binding.editTextSchool).isEmpty()) {
            binding.editTextSchool.setError("Please enter your school name");
            return false;
        } else {
            updateUserDetailsRequest.instituteName = AppUtilities.getText(binding.editTextSchool);
        }

        updateUserDetailsRequest.sClass = sClass;
        updateUserDetailsRequest.boardName = sBoard;

        return true;
    }

    private void openVerifyOTPActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VerifyOTPActivity.VERIFY_OTP_REQUEST_EXTRA, verifyOTPRequest);
        bundle.putSerializable(VerifyOTPActivity.UPDATE_USER_DETAILS_REQUEST_EXTRA, updateUserDetailsRequest);
        startTargetActivity(VerifyOTPActivity.class, bundle);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void getBoardList() {
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBoardList(), UrlConstants.URL_BOARD_LIST);
    }

    private void getClassList() {
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getClassList(), UrlConstants.URL_CLASS_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_BOARD_LIST:
                boardListResponseList = (List<BoardListResponse>) response.body();
                if (boardListResponseList.size() > 0) {
                    for (BoardListResponse boardListResponse : boardListResponseList)
                        boardList.add(boardListResponse.boardFullName + " (" + boardListResponse.boardShortName + ")");
                }
                getClassList();
                break;
            case UrlConstants.URL_CLASS_LIST:
                mProgressDialog.hideProgressDialog();
                classListResponseList = (List<ClassListResponse>) response.body();
                if (classListResponseList.size() > 0) {
                    for (ClassListResponse classListResponse : classListResponseList)
                        classList.add(classListResponse.className);
                }
                break;
            case UrlConstants.URL_CHECK_DUPLICATE_PROFILE:
                mProgressDialog.hideProgressDialog();
                CheckDuplicateProfileResponse checkDuplicateProfileResponse = (CheckDuplicateProfileResponse) response.body();
                if (checkDuplicateProfileResponse != null) {
                    if (checkDuplicateProfileResponse.retCode) {
                        verifyOTPRequest.email = checkDuplicateProfileRequest.email;
                        verifyOTPRequest.otp = checkDuplicateProfileResponse.returnData;
                        openVerifyOTPActivity();
                    } else {
                        showSnackBar(binding.rootLayout, checkDuplicateProfileResponse.returnData);
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