package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentMyLibraryBinding;
import com.app.ebook.models.book_list.BookListRequest;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.activity.LibraryBookDetailsActivity;
import com.app.ebook.ui.adapter.LibraryListAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;

public class MyLibraryFragment extends BaseFragment implements RetrofitListener, LibraryListAdapter.LibraryListItemClickListener {

    private FragmentMyLibraryBinding binding;
    private RetroClient retroClient;

    private LibraryListAdapter libraryListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_library, container, false);

        init();
        getLibraryList();

        return binding.getRoot();
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);

        libraryListAdapter = new LibraryListAdapter(getActivity(), this);
        binding.recyclerViewBookList.setAdapter(libraryListAdapter);
    }

    @Override
    public void onLibraryListItemClick(BookListResponse.ReturnResponseBean returnResponseBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LibraryBookDetailsActivity.BOOK_DETAILS_EXTRA, returnResponseBean);
        startTargetActivity(LibraryBookDetailsActivity.class, bundle);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onLibraryListItemMenuClick(View view, final BookListResponse.ReturnResponseBean returnResponseBean) {
        /*final BookItemMenuBinding bookItemMenuBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.book_item_menu, null, false);
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

            }
        });

        bookItemMenuBinding.layoutAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();

            }
        });

        mPopupWindow.showAsDropDown(view, -100, -100);*/
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(getActivity()).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getLibraryList() {
        BookListRequest bookListRequest = new BookListRequest();
        bookListRequest.userId = mUser.username;
        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getLibraryList(bookListRequest),
                UrlConstants.URL_LIBRARY_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_LIBRARY_LIST:
                mProgressDialog.hideProgressDialog();
                BookListResponse bookListResponse = (BookListResponse) response.body();
                if (bookListResponse != null && bookListResponse.retCode) {
                    ArrayList<BookListResponse.ReturnResponseBean> bookList = (ArrayList<BookListResponse.ReturnResponseBean>) bookListResponse.returnResponse;
                    if (bookList.size() > 0) {
                        libraryListAdapter.setData(bookList);
                    } else {
                        libraryListAdapter.setData(null);
                        showSnackBar(binding.rootLayout, getString(R.string.no_library_list));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.no_library_list));
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