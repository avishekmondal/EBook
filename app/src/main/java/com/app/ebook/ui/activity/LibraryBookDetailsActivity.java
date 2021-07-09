package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityLibraryBookDetailsBinding;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.ui.adapter.BookDetailsAdapter;
import com.app.ebook.util.AppUtilities;

import retrofit2.Call;

import static com.app.ebook.network.UrlConstants.BASE_URL3;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.BOOK_ID;
import static com.app.ebook.util.Constants.BOOK_NAME;
import static com.app.ebook.util.Constants.BOOK_PDF;

public class LibraryBookDetailsActivity extends BaseActivity {

    public static final String BOOK_DETAILS_EXTRA = "BookDetailsExtra";

    private ActivityLibraryBookDetailsBinding binding;
    private RetroClient retroClient;

    private BookListResponse.ReturnResponseBean returnResponseBean = new BookListResponse.ReturnResponseBean();

    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_library_book_details);

        init();
        applyTabClickEffect(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        applyTabClickEffect(1);
    }*/

    private void init() {
        if (getIntent().hasExtra(BOOK_DETAILS_EXTRA)) {
            returnResponseBean = (BookListResponse.ReturnResponseBean) getIntent().getSerializableExtra(BOOK_DETAILS_EXTRA);
            mSessionManager.setSession(BOOK_ID, returnResponseBean.bookId);
            mSessionManager.setSession(BOOK_NAME, returnResponseBean.bookName);
            mSessionManager.setSession(BOOK_PDF, BASE_URL3 + returnResponseBean.attachmentFile);

            binding.textViewTitle.setText(returnResponseBean.bookName);
        }

        pagerAdapter = new BookDetailsAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                applyTabClickEffect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickEBook(View view) {
        applyTabClickEffect(0);
    }

    public void onClickSmartCoaching(View view) {
        applyTabClickEffect(1);
    }

    public void onClickExamPreparation(View view) {
        applyTabClickEffect(2);
    }

    private void applyTabClickEffect(int position) {
        binding.buttonEBook.setTextColor(getColor(R.color.grey_EAEAEA));
        binding.buttonSmartCoaching.setTextColor(getColor(R.color.grey_EAEAEA));
        binding.buttonExamPreparation.setTextColor(getColor(R.color.grey_EAEAEA));

        binding.viewEBook.setVisibility(View.GONE);
        binding.viewSmartCoaching.setVisibility(View.GONE);
        binding.viewExamPreparation.setVisibility(View.GONE);

        switch (position) {
            case 0:
                binding.buttonEBook.setTextColor(getColor(R.color.white));
                binding.viewEBook.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.buttonSmartCoaching.setTextColor(getColor(R.color.white));
                binding.viewSmartCoaching.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.buttonExamPreparation.setTextColor(getColor(R.color.white));
                binding.viewExamPreparation.setVisibility(View.VISIBLE);
                break;
        }

        binding.viewPager.setCurrentItem(position);
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    /*private void getChapterList() {
        BookChapterListRequest bookChapterListRequest = new BookChapterListRequest();
        bookChapterListRequest.bookId = returnResponseBean.bookId;

        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBookChapterList(bookChapterListRequest),
                URL_BOOK_CHAPTER_LIST);
    }

    *//*private void getBookDetails() {
        BookContentRequest bookContentRequest = new BookContentRequest();
        bookContentRequest.username = mUser.username;
        bookContentRequest.bookId = mSessionManager.getSession(BOOK_ID);

        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBookContent(bookContentRequest),
                URL_BOOK_CONTENT);
    }*//*

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        switch (method_name) {
            *//*case URL_BOOK_CONTENT:
                BookContentResponse bookContentResponse = (BookContentResponse) response.body();
                if (bookContentResponse != null && bookContentResponse.retCode) {
                    binding.rootLayout.setVisibility(View.VISIBLE);
                    returnResponseBean = bookContentResponse.returnResponse;
                    binding.textViewBookName.setText(returnResponseBean.bookName);
                    binding.textViewAuthor.setText("By " + returnResponseBean.authorName);
                    binding.textViewClass.setText("Class "  + returnResponseBean.sclass);
                    binding.textViewBoard.setText(returnResponseBean.board);
                    binding.textViewPrice.setText("Rs. " + returnResponseBean.bookPrice);

                    if (bookContentResponse.isSubscribed)
                        binding.buttonSubscribe.setText("Unsubscribe");
                    else
                        binding.buttonSubscribe.setText("Subscribe Now");

                    setImage(this, binding.imageViewBook, BASE_URL2 + bookContentResponse.returnResponse.coverPhoto);
                } else
                    showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                break;*//*
            case UrlConstants.URL_BOOK_CHAPTER_LIST:
                BookChapterListResponse chapterListResponse = (BookChapterListResponse) response.body();
                if (chapterListResponse != null && chapterListResponse.retCode) {
                    List<ReturnData> chapterList = chapterListResponse.returnData;
                    if (chapterList.size() > 0) {
                        ChapterAdapter chapterAdapter = new ChapterAdapter(LibraryBookDetailsActivity.this, chapterList);
                        //binding.recyclerViewChapterList.setAdapter(chapterAdapter);
                    } else {
                        showSnackBar(binding.rootLayout, getString(R.string.no_coaching));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.no_coaching));
                }
                break;
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }*/
}