package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentSubscriptionBinding;
import com.app.ebook.models.book_subscription.SubscriptionListRequest;
import com.app.ebook.models.book_subscription.SubscriptionListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.SubscriptionListAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;

public class SubscriptionFragment extends BaseFragment implements RetrofitListener {

    private FragmentSubscriptionBinding binding;
    private RetroClient retroClient;

    private SubscriptionListAdapter subscriptionListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscription, container, false);

        init();
        getSubscriptionList();

        return binding.getRoot();
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);

        subscriptionListAdapter = new SubscriptionListAdapter(getActivity());
        binding.recyclerViewSubscriptionList.setAdapter(subscriptionListAdapter);
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(getActivity()).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getSubscriptionList() {
        SubscriptionListRequest subscriptionListRequest = new SubscriptionListRequest();
        subscriptionListRequest.userId = mUser.username;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getSubscriptionList(subscriptionListRequest),
                UrlConstants.URL_SUBSCRIPTION_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        SubscriptionListResponse subscriptionListResponse = (SubscriptionListResponse) response.body();
        if (subscriptionListResponse != null && subscriptionListResponse.retCode) {
            ArrayList<SubscriptionListResponse.ReturnDataBean> subscriptionList = (ArrayList<SubscriptionListResponse.ReturnDataBean>) subscriptionListResponse.returnData;
            if (subscriptionList.size() > 0) {
                subscriptionListAdapter.setData(subscriptionList);
            } else {
                subscriptionListAdapter.setData(null);
                showSnackBar(binding.rootLayout, getString(R.string.no_subscription_list));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_subscription_list));
        }

    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, errorMessage);
    }
}