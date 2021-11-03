package com.app.ebook.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityBookListBinding;
import com.app.ebook.databinding.BookItemMenuBinding;
import com.app.ebook.models.book_list.BookListRequest;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.models.wish_list.AddToWishListRequest;
import com.app.ebook.models.wish_list.AddToWishListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.BookListAdapter;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;

public class BookListActivity extends BaseActivity implements RetrofitListener, BookListAdapter.BookListItemClickListener {

    public static final String BOOK_LIST_REQUEST_EXTRA = "BookListRequestExtra";

    private ActivityBookListBinding binding;
    private RetroClient retroClient;

    private BookListRequest bookListRequest = new BookListRequest();
    private BookListAdapter bookListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_list);

        init();
        getBookList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        bookListAdapter = new BookListAdapter(this, this);
        binding.recyclerViewBookList.setAdapter(bookListAdapter);

        if (getIntent().hasExtra(BOOK_LIST_REQUEST_EXTRA))
            bookListRequest = (BookListRequest) getIntent().getSerializableExtra(BOOK_LIST_REQUEST_EXTRA);
        Log.v("aa", new Gson().toJson(bookListRequest));
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickCart(View view) {
        goToCartActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getBookList();
        }
    }

    @Override
    public void onBookListItemClick(BookListResponse.ReturnResponseBean returnResponseBean) {
        if (!returnResponseBean.isSubscribed) {
            mSessionManager.setSession(Constants.IS_SUBSCRIBED, false);
            mSessionManager.setSession(Constants.BOOK_DETAILS, new Gson().toJson(returnResponseBean, BookListResponse.ReturnResponseBean.class));
            Intent intent = new Intent(this, BookDetailsActivity.class);
            startActivityForResult(intent, 1);
        } else {
            mSessionManager.setSession(Constants.IS_SUBSCRIBED, true);
            mSessionManager.setSession(Constants.BOOK_DETAILS, new Gson().toJson(returnResponseBean, BookListResponse.ReturnResponseBean.class));
            startTargetActivity(LibraryBookDetailsActivity.class);
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBookListItemMenuClick(View view, final BookListResponse.ReturnResponseBean returnResponseBean) {
        if (returnResponseBean.isWishlist && returnResponseBean.isSubscribed) {
            AppUtilities.showToast(this, "No Options Available");
        } else {
            final BookItemMenuBinding bookItemMenuBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.book_item_menu, null, false);
            final PopupWindow mPopupWindow = new PopupWindow(bookItemMenuBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            if (returnResponseBean.isWishlist)
                bookItemMenuBinding.layoutWishList.setVisibility(View.GONE);
            if (returnResponseBean.isSubscribed) {
                bookItemMenuBinding.viewWishlist.setVisibility(View.GONE);
                bookItemMenuBinding.layoutSubscribe.setVisibility(View.GONE);
                bookItemMenuBinding.layoutAddToCart.setVisibility(View.GONE);
            }

            bookItemMenuBinding.layoutWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                    addWishList(returnResponseBean.bookId);
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
    }

    private void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getBookList() {
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBookList(bookListRequest),
                UrlConstants.URL_BOOK_LIST);
    }

    private void addWishList(String bookID) {
        AddToWishListRequest addToWishListRequest = new AddToWishListRequest();
        addToWishListRequest.username = mUser.username;
        addToWishListRequest.wishlist = bookID;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).addWishList(addToWishListRequest),
                UrlConstants.URL_ADD_WISH_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_BOOK_LIST:
                mProgressDialog.hideProgressDialog();
                BookListResponse bookListResponse = (BookListResponse) response.body();
                if (bookListResponse != null && bookListResponse.retCode) {
                    ArrayList<BookListResponse.ReturnResponseBean> bookList = (ArrayList<BookListResponse.ReturnResponseBean>) bookListResponse.returnResponse;
                    if (bookList.size() > 0) {
                        bookListAdapter.setData(bookList);
                    } else {
                        bookListAdapter.setData(null);
                        showSnackBar(binding.rootLayout, getString(R.string.no_books));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.no_books));
                }
                break;
            case UrlConstants.URL_ADD_WISH_LIST:
                AddToWishListResponse addToWishListResponse = (AddToWishListResponse) response.body();
                if (addToWishListResponse != null && addToWishListResponse.retCode) {
                    showSnackBar(binding.rootLayout, "Book added to WishList");
                    getBookList();
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