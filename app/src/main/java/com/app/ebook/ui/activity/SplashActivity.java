package com.app.ebook.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivitySplashBinding;
import com.app.ebook.models.BoardListResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.IS_LOGGEDIN;

public class SplashActivity extends BaseActivity implements RetrofitListener {

    private ActivitySplashBinding binding;
    private RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        init();
        getBoardList();
    }

    private void init() {
        retroClient = new RetroClient(this, this);
    }

    private void goToNextActivity(Class activity) {
        startActivity(new Intent(this, activity));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getBoardList() {
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBoardList(),
                UrlConstants.URL_BOARD_LIST);
    }

    private void getClassList() {
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getClassList(),
                UrlConstants.URL_CLASS_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_BOARD_LIST:
                mSessionManager.setSession(Constants.BOARD_LIST, new Gson().toJson(response.body(),
                        new TypeToken<List<BoardListResponse>>() {}.getType()));

                getClassList();
                break;
            case UrlConstants.URL_CLASS_LIST:
                mSessionManager.setSession(Constants.CLASS_LIST, new Gson().toJson(response.body(),
                        new TypeToken<List<ClassListResponse>>() {}.getType()));

                if (mSessionManager.getBooleanSession(IS_LOGGEDIN))
                    goToNextActivity(MainActivity.class);
                else
                    goToNextActivity(IntroSliderActivity.class);
                break;
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}

/*https://www.getpostman.com/collections/944ccbce459794d2c802*/
