package com.app.ebook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.ebook.R;
import com.app.ebook.models.BoardListResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.models.UserDetailsResponse;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.ui.activity.CartActivity;
import com.app.ebook.ui.activity.SubscriptionPlanActivity;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.ProgressDialog;
import com.app.ebook.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {

    public SessionManager mSessionManager;
    public ProgressDialog mProgressDialog;
    public List<BoardListResponse> mBoardList = new ArrayList<>();
    public List<ClassListResponse>mClassList = new ArrayList<>();
    public UserDetailsResponse mUser;
    public BookListResponse.ReturnResponseBean mBookDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(getActivity());
        mProgressDialog = new ProgressDialog(getActivity(), "", false);
        mBoardList = AppUtilities.getBoardList(getActivity());
        mClassList = AppUtilities.getClassList(getActivity());
        mUser = AppUtilities.getUser(getActivity());
        mBookDetails = AppUtilities.getBookDetails(getActivity());
    }

    public void startTargetActivity(Class targetClass) {
        Intent mIntent = new Intent(getActivity(), targetClass);
        startActivity(mIntent);
    }

    public void startTargetActivity(Class targetClass, Bundle mBundle) {
        Intent mIntent = new Intent(getActivity(), targetClass);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    public void startTargetActivityNewTask(Class targetClass) {
        Intent mIntent = new Intent(getActivity(), targetClass);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
        getActivity().finish();
    }

    public void goToCartActivity() {
        startTargetActivity(CartActivity.class);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void goToSubscriptionPlanActivity() {
        startTargetActivity(SubscriptionPlanActivity.class);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
