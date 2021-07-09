package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import com.amar.library.ui.interfaces.IScrollViewListener;
import com.app.ebook.R;
import com.app.ebook.databinding.ActivityBookDetailsBinding;
import com.app.ebook.models.book_chapters.BookChapterListRequest;
import com.app.ebook.models.book_chapters.BookChapterListResponse;
import com.app.ebook.models.book_chapters.ReturnData;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.models.wish_list.AddToWishListRequest;
import com.app.ebook.models.wish_list.AddToWishListResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.ChapterAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.BASE_URL2;
import static com.app.ebook.network.UrlConstants.URL_BOOK_CHAPTER_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.BOOK_ID;
import static com.app.ebook.util.Constants.BOOK_NAME;
import static com.app.ebook.util.Constants.KEY;

public class BookDetailsActivity extends BaseActivity implements RetrofitListener {

    public static final String BOOK_DETAILS_EXTRA = "BookDetailsExtra";

    private ActivityBookDetailsBinding binding;
    private RetroClient retroClient;

    private BookListResponse.ReturnResponseBean returnResponseBean = new BookListResponse.ReturnResponseBean();
    private final AddToWishListRequest addToWishListRequest = new AddToWishListRequest();

    private boolean isDataChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_details);

        init();
        getChapterList();
    }

    @Override
    public void onBackPressed() {
        if (isDataChanged) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyTabClickEffect(1);
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        if (getIntent().hasExtra(BOOK_DETAILS_EXTRA)) {
            returnResponseBean = (BookListResponse.ReturnResponseBean) getIntent().getSerializableExtra(BOOK_DETAILS_EXTRA);
            mSessionManager.setSession(BOOK_ID, returnResponseBean.bookId);
            mSessionManager.setSession(BOOK_NAME, returnResponseBean.bookName);
            setBookDetails();
        }
    }

    private void setBookDetails() {
        binding.textViewBookName.setText(returnResponseBean.bookName);
        binding.textViewAuthor.setText("By " + returnResponseBean.authorName);
        binding.textViewClass.setText("Class " + AppUtilities.getClassName(this, returnResponseBean.sclass));
        binding.textViewBoard.setText(returnResponseBean.board);
        binding.textViewPrice.setText("Rs. " + returnResponseBean.bookPrice);

        if (returnResponseBean.isWishlist)
            binding.imageViewWishList.setImageResource(R.drawable.ic_shortlist_fill);
        else
            binding.imageViewWishList.setImageResource(R.drawable.ic_shortlist);

        if (returnResponseBean.isSubscribed)
            binding.buttonSubscribe.setText("Subscribed");
        else
            binding.buttonSubscribe.setText("Subscribe Now");

        AppUtilities.loadImage(this, binding.imageViewBook, BASE_URL2 + returnResponseBean.coverPhoto);

        binding.stickyScrollView.setScrollViewListener(new IScrollViewListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (binding.stickyScrollView.isHeaderSticky()) {
                    binding.layoutToolBar.setBackgroundColor(getColor(R.color.colorPrimary));
                    binding.textViewTitle.setText(returnResponseBean.bookName);
                    binding.imageViewWishList.setVisibility(View.GONE);
                    binding.imageViewCart.setVisibility(View.GONE);
                } else {
                    binding.layoutToolBar.setBackgroundColor(getColor(R.color.transparent));
                    binding.textViewTitle.setText("");
                    binding.imageViewWishList.setVisibility(View.VISIBLE);
                    binding.imageViewCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStopped(boolean isStoped) {

            }
        });
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickWishList(View view) {
        addToWishListRequest.username = mUser.username;
        addToWishListRequest.wishlist = returnResponseBean.bookId;

        if (!returnResponseBean.isWishlist) {
            makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).addWishList(addToWishListRequest),
                    UrlConstants.URL_ADD_WISH_LIST);
        } else {
            makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).deleteWishList(addToWishListRequest),
                    UrlConstants.URL_DELETE_WISH_LIST);
        }
    }

    public void onClickSubscribe(View view) {
        if (returnResponseBean.isSubscribed)
            AppUtilities.showToast(this, "Already Subscribed");
        else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(SubscriptionPlanActivity.BOOK_DETAILS_EXTRA, returnResponseBean);
            startTargetActivity(SubscriptionPlanActivity.class, bundle);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickEBook(View view) {
        applyTabClickEffect(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putSerializable(EBookActivity.BOOK_DETAILS_EXTRA, returnResponseBean);
                startTargetActivity(EBookActivity.class, bundle);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, 300);
    }

    public void onClickSmartCoaching(View view) {
        applyTabClickEffect(1);
    }

    public void onClickExamPreparation(View view) {
        applyTabClickEffect(2);

        PopupMenu popup = new PopupMenu(BookDetailsActivity.this, binding.layoutExamPreparation);
        popup.getMenuInflater().inflate(R.menu.exam_preparation, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSessionManager.setSession(KEY, String.valueOf(item.getTitle()));
                startTargetActivity(ExamPrepChapterActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
        });
    }

    private void applyTabClickEffect(int position) {
        binding.viewEBook.setVisibility(View.GONE);
        binding.viewSmartCoaching.setVisibility(View.GONE);
        binding.viewExamPreparation.setVisibility(View.GONE);

        switch (position) {
            case 0:
                binding.viewEBook.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.viewSmartCoaching.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.viewExamPreparation.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getChapterList() {
        BookChapterListRequest bookChapterListRequest = new BookChapterListRequest();
        bookChapterListRequest.bookId = returnResponseBean.bookId;

        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBookChapterList(bookChapterListRequest),
                URL_BOOK_CHAPTER_LIST);
    }

    /*private void getBookDetails() {
        BookContentRequest bookContentRequest = new BookContentRequest();
        bookContentRequest.username = mUser.username;
        bookContentRequest.bookId = mSessionManager.getSession(BOOK_ID);

        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBookContent(bookContentRequest),
                URL_BOOK_CONTENT);
    }*/

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        switch (method_name) {
            /*case URL_BOOK_CONTENT:
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
                break;*/
            case UrlConstants.URL_BOOK_CHAPTER_LIST:
                BookChapterListResponse chapterListResponse = (BookChapterListResponse) response.body();
                if (chapterListResponse != null && chapterListResponse.retCode) {
                    List<ReturnData> chapterList = chapterListResponse.returnData;
                    if (chapterList.size() > 0) {
                        ChapterAdapter chapterAdapter = new ChapterAdapter(BookDetailsActivity.this, chapterList);
                        binding.recyclerViewChapterList.setAdapter(chapterAdapter);
                    } else {
                        showSnackBar(binding.rootLayout, getString(R.string.no_coaching));
                    }
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.no_coaching));
                }
                break;
            case UrlConstants.URL_ADD_WISH_LIST:
                AddToWishListResponse addToWishListResponse = (AddToWishListResponse) response.body();
                if (addToWishListResponse != null && addToWishListResponse.retCode) {
                    isDataChanged = true;
                    returnResponseBean.isWishlist = true;
                    binding.imageViewWishList.setImageResource(R.drawable.ic_shortlist_fill);
                    showSnackBar(binding.rootLayout, "Book added to WishList");
                } else {
                    showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
                }
                break;
            case UrlConstants.URL_DELETE_WISH_LIST:
                addToWishListResponse = (AddToWishListResponse) response.body();
                if (addToWishListResponse != null && addToWishListResponse.retCode) {
                    isDataChanged = true;
                    returnResponseBean.isWishlist = false;
                    binding.imageViewWishList.setImageResource(R.drawable.ic_shortlist);
                    showSnackBar(binding.rootLayout, "Book removed from WishList");
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