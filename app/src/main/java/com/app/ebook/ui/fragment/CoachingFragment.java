package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentCoachingBinding;
import com.app.ebook.models.book_chapters.BookChapterListRequest;
import com.app.ebook.models.book_chapters.BookChapterListResponse;
import com.app.ebook.models.book_chapters.ReturnData;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.ui.adapter.ChapterAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.network.UrlConstants.URL_BOOK_CHAPTER_LIST;
import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.BOOK_ID;

public class CoachingFragment extends BaseFragment implements RetrofitListener {

    private FragmentCoachingBinding binding;

    private RetroClient retroClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coaching, container, false);

        init();
        initClickListener();
        getChapterList();

        return binding.getRoot();
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);
    }

    private void initClickListener() {

    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(getActivity()).isOnline()) {
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    private void getChapterList() {
        BookChapterListRequest bookChapterListRequest = new BookChapterListRequest();
        bookChapterListRequest.bookId = mSessionManager.getSession(BOOK_ID);

        makeNetworkCall(retroClient.retrofit.create(RetroClient.RestInterface.class).getBookChapterList(bookChapterListRequest),
                URL_BOOK_CHAPTER_LIST);
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        switch (method_name) {
            case UrlConstants.URL_BOOK_CHAPTER_LIST:
                BookChapterListResponse chapterListResponse = (BookChapterListResponse) response.body();
                if (chapterListResponse != null && chapterListResponse.retCode) {
                    List<ReturnData> chapterList = chapterListResponse.returnData;
                    if (chapterList.size() > 0) {
                        ChapterAdapter chapterAdapter = new ChapterAdapter(getActivity(), chapterList);
                        binding.recyclerViewChapterList.setAdapter(chapterAdapter);
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
    }
}