package com.app.ebook.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.ebook.models.BoardListResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.models.UserDetailsResponse;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.ProgressDialog;
import com.app.ebook.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    public SessionManager mSessionManager;
    public List<BoardListResponse> mBoardList = new ArrayList<>();
    public List<ClassListResponse>mClassList = new ArrayList<>();
    public UserDetailsResponse mUser;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mProgressDialog = new ProgressDialog(this, "", false);
        mSessionManager = new SessionManager(this);
        mBoardList = AppUtilities.getBoardList(this);
        mClassList = AppUtilities.getClassList(this);
        mUser = AppUtilities.getUser(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startTargetActivity(Class targetClass) {
        Intent mIntent = new Intent(this, targetClass);
        startActivity(mIntent);
    }

    public void startTargetActivity(Class targetClass, Bundle mBundle) {
        Intent mIntent = new Intent(this, targetClass);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    public void startTargetActivityNewTask(Class targetClass) {
        Intent mIntent = new Intent(this, targetClass);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
        finish();
    }

    public void startTargetActivityNewTask(Class targetClass, Bundle mBundle) {
        Intent mIntent = new Intent(this, targetClass);
        mIntent.putExtras(mBundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
        finish();
    }

}
