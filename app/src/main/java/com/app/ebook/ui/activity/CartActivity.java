package com.app.ebook.ui.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityCartBinding;
import com.app.ebook.databinding.DialogRemoveBinding;
import com.app.ebook.models.cart.CartListRequest;
import com.app.ebook.models.cart.CartListResponse;
import com.app.ebook.models.cart.UpdateCartRequest;
import com.app.ebook.models.cart.UpdateCartResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.adapter.CartAdapter;
import com.app.ebook.util.AppUtilities;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.URL_CART_LIST;
import static com.app.ebook.network.UrlConstants.URL_DELETE_CART;
import static com.app.ebook.util.AppUtilities.showSnackBar;

public class CartActivity extends BaseActivity implements RetrofitListener, CartAdapter.CartItemClickListener {

    private ActivityCartBinding binding;
    private RetroClient retroClient;

    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);

        init();
        getCartList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        cartAdapter = new CartAdapter(this, this);
        binding.recyclerViewCartList.setAdapter(cartAdapter);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickProceedToCheckout(View view) {
        if (cartAdapter.getSelectedCartList().size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(OrderSummaryActivity.ORDER_SUMMARY_EXTRA, (Serializable) cartAdapter.getSelectedCartList());
            startTargetActivity(OrderSummaryActivity.class, bundle);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.select_atleast_one));
        }
    }

    @Override
    public void onCartItemDeleteClick(final String productID) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final DialogRemoveBinding dialogRemoveBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.dialog_remove, null, false);
        dialog.setContentView(dialogRemoveBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialogRemoveBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogRemoveBinding.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                deleteFromCart(productID);
            }
        });

        dialog.show();
    }

    private void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getCartList() {
        CartListRequest cartListRequest = new CartListRequest();
        cartListRequest.userId = mUser.username;

        retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getCartList(cartListRequest),
                URL_CART_LIST);
    }

    private void deleteFromCart(String productID) {
        UpdateCartRequest updateCartRequest = new UpdateCartRequest();
        updateCartRequest.productId = productID;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).deleteFromCart(updateCartRequest),
                URL_DELETE_CART);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case URL_CART_LIST:
                mProgressDialog.hideProgressDialog();
                CartListResponse cartListResponse = (CartListResponse) response.body();
                if (cartListResponse != null && cartListResponse.retCode) {
                    ArrayList<CartListResponse.ReturnDataBean> cartList = (ArrayList<CartListResponse.ReturnDataBean>) cartListResponse.returnData;
                    if (cartList.size() > 0) {
                        binding.layoutCart.setVisibility(View.VISIBLE);
                        cartAdapter.setData(cartList);
                        binding.textViewSubTotal.setText("Rs. " + cartListResponse.totalPrice);
                    } else {
                        binding.layoutCart.setVisibility(View.GONE);
                        showSnackBar(binding.rootLayout, getString(R.string.no_cart));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.no_cart));
                }
                break;
            case URL_DELETE_CART:
                UpdateCartResponse updateCartResponse = (UpdateCartResponse) response.body();
                if (updateCartResponse != null) {
                    showSnackBar(binding.rootLayout, updateCartResponse.message);
                    if (updateCartResponse.retCode) {
                        getCartList();
                    } else {
                        mProgressDialog.hideProgressDialog();
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
