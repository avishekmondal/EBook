package com.app.ebook.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.BookItemMenuBinding;
import com.app.ebook.databinding.FragmentWishlistBinding;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.models.wish_list.AddToWishListRequest;
import com.app.ebook.models.wish_list.AddToWishListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.activity.BookDetailsActivity;
import com.app.ebook.ui.activity.LibraryBookDetailsActivity;
import com.app.ebook.ui.activity.SubscriptionPlanActivity;
import com.app.ebook.ui.adapter.BookListAdapter;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;

public class WishListFragment extends BaseFragment implements RetrofitListener, BookListAdapter.BookListItemClickListener {

    private FragmentWishlistBinding binding;
    private RetroClient retroClient;

    private BookListAdapter bookListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, container, false);

        init();
        getWishList();

        return binding.getRoot();
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);

        bookListAdapter = new BookListAdapter(getActivity(), this);
        binding.recyclerViewBookList.setAdapter(bookListAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getWishList();
        }
    }

    @Override
    public void onBookListItemClick(BookListResponse.ReturnResponseBean returnResponseBean) {
        if (!returnResponseBean.isSubscribed) {
            mSessionManager.setSession(Constants.IS_SUBSCRIBED, false);
            mSessionManager.setSession(Constants.BOOK_DETAILS, new Gson().toJson(returnResponseBean, BookListResponse.ReturnResponseBean.class));
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            startActivityForResult(intent, 1);
        } else {
            mSessionManager.setSession(Constants.IS_SUBSCRIBED, true);
            mSessionManager.setSession(Constants.BOOK_DETAILS, new Gson().toJson(returnResponseBean, BookListResponse.ReturnResponseBean.class));
            startTargetActivity(LibraryBookDetailsActivity.class);
        }
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBookListItemMenuClick(View view, final BookListResponse.ReturnResponseBean returnResponseBean) {
        final BookItemMenuBinding bookItemMenuBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.book_item_menu, null, false);
        final PopupWindow mPopupWindow = new PopupWindow(bookItemMenuBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (returnResponseBean.isWishlist) {
            bookItemMenuBinding.imageViewWishList.setPadding(8,8,8,8);
            bookItemMenuBinding.imageViewWishList.setImageResource(R.drawable.ic_delete);
            bookItemMenuBinding.textViewWishListTitle.setText("Remove");
            bookItemMenuBinding.textViewWishListSubTitle.setText("Remove from list");
        } else {
            bookItemMenuBinding.layoutWishList.setVisibility(View.GONE);
        }

        if (returnResponseBean.isSubscribed) {
            bookItemMenuBinding.viewWishlist.setVisibility(View.GONE);
            bookItemMenuBinding.layoutSubscribe.setVisibility(View.GONE);
            bookItemMenuBinding.layoutAddToCart.setVisibility(View.GONE);
        }

        bookItemMenuBinding.layoutWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                deleteWishList(returnResponseBean.bookId);
            }
        });

        bookItemMenuBinding.layoutSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                mSessionManager.setSession(Constants.BOOK_DETAILS, new Gson().toJson(returnResponseBean, BookListResponse.ReturnResponseBean.class));
                goToSubscriptionPlanActivity();
            }
        });

        bookItemMenuBinding.layoutAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                mSessionManager.setSession(Constants.BOOK_DETAILS, new Gson().toJson(returnResponseBean, BookListResponse.ReturnResponseBean.class));
                goToSubscriptionPlanActivity();
            }
        });

        mPopupWindow.showAsDropDown(view, -100, -100);
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(getActivity()).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getWishList() {
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getWishList(),
                UrlConstants.URL_WISH_LIST);
    }

    private void deleteWishList(String bookID) {
        AddToWishListRequest addToWishListRequest = new AddToWishListRequest();
        addToWishListRequest.username = mUser.username;
        addToWishListRequest.wishlist = bookID;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).deleteWishList(addToWishListRequest),
                UrlConstants.URL_DELETE_WISH_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_WISH_LIST:
                mProgressDialog.hideProgressDialog();
                BookListResponse bookListResponse = (BookListResponse) response.body();
                if (bookListResponse != null && bookListResponse.retCode) {
                    ArrayList<BookListResponse.ReturnResponseBean> bookList = (ArrayList<BookListResponse.ReturnResponseBean>) bookListResponse.returnResponse;
                    if (bookList.size() > 0) {
                        bookListAdapter.setData(bookList);
                    } else {
                        bookListAdapter.setData(null);
                        showSnackBar(binding.rootLayout, getString(R.string.no_wish_list));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.no_wish_list));
                }
                break;
            case UrlConstants.URL_DELETE_WISH_LIST:
                AddToWishListResponse addToWishListResponse = (AddToWishListResponse) response.body();
                if (addToWishListResponse != null && addToWishListResponse.retCode) {
                    showSnackBar(binding.rootLayout, "Book removed from WishList");
                    getWishList();
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