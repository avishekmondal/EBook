package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.app.ebook.models.CityListRequest;
import com.app.ebook.models.CityListResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.models.StateListResponse;
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
    ArrayList<String> stateList = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    private ListPopupWindow popupWindow = null;

    private String sClass = "", sBoard = "", sState = "", sCity = "";

    private List<StateListResponse> stateListResponseList = new ArrayList<>();
    private List<CityListResponse.ReturnData> cityListResponseList = new ArrayList<>();

    private final CheckDuplicateProfileRequest checkDuplicateProfileRequest = new CheckDuplicateProfileRequest();
    private final VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();
    private final UpdateUserDetailsRequest updateUserDetailsRequest = new UpdateUserDetailsRequest();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        init();
        initClickListener();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUser = AppUtilities.getUser(getActivity());

        binding.layoutClass.setEnabled(false);
        binding.editTextName.setText(mUser.name);
        binding.textViewClass.setText(sClass = mUser.sClass);
        binding.editTextEmail.setText(mUser.email);
        binding.editTextMobile.setText(mUser.mobile);
        binding.textViewClass.setText(!TextUtils.isEmpty(mUser.sClass) ? sClass = mUser.sClass : " - ");
        binding.editTextBoard.setText(!TextUtils.isEmpty(mUser.boardName) ? sBoard = mUser.boardName : " - ");
        binding.editTextSchool.setText(!TextUtils.isEmpty(mUser.instituteName) ? mUser.instituteName : " - ");
        binding.editTextState.setText(!TextUtils.isEmpty(mUser.state) ? sState = mUser.state : " - ");
        binding.editTextCity.setText(!TextUtils.isEmpty(mUser.state) ? sCity = mUser.city : " - ");
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);

        for (BoardListResponse boardListResponse : mBoardList)
            boardList.add(boardListResponse.boardFullName + " (" + boardListResponse.boardShortName + ")");

        for (ClassListResponse classListResponse : mClassList)
            classList.add(classListResponse.className);
    }

    private void initClickListener() {
        binding.layoutClass.setOnClickListener(view -> {
            popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                    classList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    //sClass = mClassList.get(position).classId;
                    binding.textViewClass.setText(sClass = mClassList.get(position).className);
                }
            });
            popupWindow.show();
        });

        binding.editTextBoard.setOnClickListener(view -> {
            binding.editTextBoard.setError(null);
            popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                    boardList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    binding.editTextBoard.setText(sBoard = mBoardList.get(position).boardShortName);
                }
            });
            popupWindow.show();
        });

        binding.editTextState.setOnClickListener(view -> {
            binding.editTextState.setError(null);
            popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                    stateList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    if (!sState.equals(stateList.get(position))) {
                        binding.editTextState.setText(sState = stateList.get(position));
                        binding.editTextCity.setText(sCity = "");
                        getCityList();
                    }
                }
            });
            popupWindow.show();
        });

        binding.editTextCity.setOnClickListener(view -> {
            binding.editTextCity.setError(null);
            if (!TextUtils.isEmpty(sState)) {
                popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                        cityList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        binding.editTextCity.setText(sCity = cityList.get(position));
                    }
                });
                popupWindow.show();
            } else {
                showSnackBar(binding.rootLayout, "Please select your state first");
            }
        });

        binding.buttonEdit.setOnClickListener(view -> {
            setFieldsEditable(true);
        });

        binding.buttonSave.setOnClickListener(view -> {
            if (isAllFieldsValid()) {
                makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).checkDuplicateProfile(checkDuplicateProfileRequest), UrlConstants.URL_CHECK_DUPLICATE_PROFILE);
            }
        });
    }

    public void setFieldsEditable(boolean isEditable) {
        binding.buttonEdit.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        binding.buttonSave.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        binding.imageViewDropDownClass.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        binding.layoutClass.setEnabled(isEditable ? true : false);
        binding.editTextBoard.setEnabled(isEditable ? true : false);
        binding.editTextSchool.setEnabled(isEditable ? true : false);
        binding.editTextState.setEnabled(isEditable ? true : false);
        binding.editTextCity.setEnabled(isEditable ? true : false);

        if (isEditable) {
            binding.textViewClass.setText(!TextUtils.isEmpty(mUser.sClass) ? mUser.sClass : "");
            binding.editTextBoard.setText(!TextUtils.isEmpty(mUser.boardName) ? mUser.boardName : "");
            binding.editTextSchool.setText(!TextUtils.isEmpty(mUser.instituteName) ? mUser.instituteName : "");
            binding.editTextState.setText(!TextUtils.isEmpty(mUser.state) ? mUser.state : "");
            binding.editTextCity.setText(!TextUtils.isEmpty(mUser.state) ? mUser.city : "");

            if(stateListResponseList.size() == 0) {
                getStateList();
            }
        }
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
        updateUserDetailsRequest.username = checkDuplicateProfileRequest.username = mUser.username;

        if (sClass.isEmpty()) {
            showSnackBar(binding.rootLayout, "Please select your class");
            return false;
        } else {
            updateUserDetailsRequest.sClass = sClass;
        }

        if (sBoard.isEmpty()) {
            showSnackBar(binding.rootLayout, "Please select your board");
            return false;
        } else {
            updateUserDetailsRequest.boardName = sBoard;
        }

        if (AppUtilities.getText(binding.editTextSchool).isEmpty()) {
            showSnackBar(binding.rootLayout, "Please enter your school name");
            return false;
        } else {
            updateUserDetailsRequest.instituteName = AppUtilities.getText(binding.editTextSchool);
        }

        if (!sState.isEmpty()) {
            updateUserDetailsRequest.state = sState;
        }

        if (!sCity.isEmpty()) {
            updateUserDetailsRequest.city = sCity;
        }

        return true;
    }

    private void openVerifyOTPActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(VerifyOTPActivity.VERIFY_OTP_REQUEST_EXTRA, verifyOTPRequest);
        bundle.putSerializable(VerifyOTPActivity.UPDATE_USER_DETAILS_REQUEST_EXTRA, updateUserDetailsRequest);
        startTargetActivity(VerifyOTPActivity.class, bundle);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setFieldsEditable(false);
    }

    private void getStateList() {
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getStateList(), UrlConstants.URL_STATE_LIST);
    }

    private void getCityList() {
        cityListResponseList = new ArrayList<>();
        cityList = new ArrayList<>();
        CityListRequest cityListRequest = new CityListRequest();
        cityListRequest.stateName = sState;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getCityList(cityListRequest), UrlConstants.URL_CITY_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_STATE_LIST:
                stateListResponseList = (List<StateListResponse>) response.body();
                if (stateListResponseList.size() > 0) {
                    for (StateListResponse stateListResponse : stateListResponseList)
                        stateList.add(stateListResponse.stateName);
                }

                if (!TextUtils.isEmpty(sState) && cityListResponseList.size() == 0) {
                    getCityList();
                } else {
                    mProgressDialog.hideProgressDialog();
                }
                break;
            case UrlConstants.URL_CITY_LIST:
                mProgressDialog.hideProgressDialog();
                CityListResponse cityListResponse = (CityListResponse) response.body();
                if (cityListResponse != null) {
                    if (cityListResponse.retCode) {
                        cityListResponseList = cityListResponse.returnData;
                        for (CityListResponse.ReturnData returnData : cityListResponseList)
                            cityList.add(returnData.cityName);
                    } else {
                        showSnackBar(binding.rootLayout, cityListResponse.details);
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                }
                break;
            case UrlConstants.URL_CHECK_DUPLICATE_PROFILE:
                mProgressDialog.hideProgressDialog();
                CheckDuplicateProfileResponse checkDuplicateProfileResponse = (CheckDuplicateProfileResponse) response.body();
                if (checkDuplicateProfileResponse != null) {
                    if (checkDuplicateProfileResponse.retCode) {
                        verifyOTPRequest.email = mUser.email;
                        verifyOTPRequest.otp = checkDuplicateProfileResponse.returnOtp;
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