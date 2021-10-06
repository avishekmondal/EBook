package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentHomeBinding;
import com.app.ebook.models.BoardListResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.models.book_list.BookListRequest;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.activity.BookListActivity;
import com.app.ebook.ui.adapter.BookHorizontalListAdapter;
import com.app.ebook.util.AppUtilities;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;

public class HomeFragment extends BaseFragment implements RetrofitListener {

    private FragmentHomeBinding binding;
    private RetroClient retroClient;

    ArrayList<String> boardList = new ArrayList<>();
    ArrayList<String> classList = new ArrayList<>();
    private ListPopupWindow popupWindow = null;

    private final BookListRequest bookListRequest = new BookListRequest();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        init();
        initClickListener();

        return binding.getRoot();
    }

    private void init() {
        retroClient = new RetroClient(getActivity(), this);

        for (BoardListResponse boardListResponse : mBoardList)
            boardList.add(boardListResponse.boardFullName + " (" + boardListResponse.boardShortName + ")");

        for (ClassListResponse classListResponse : mClassList)
            classList.add(classListResponse.className);

        BookHorizontalListAdapter bookAdapter = new BookHorizontalListAdapter(getContext());
        binding.recyclerViewBookList1.setAdapter(bookAdapter);
        binding.recyclerViewBookList1.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerViewBookList2.setAdapter(bookAdapter);
        binding.recyclerViewBookList2.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerViewBookList3.setAdapter(bookAdapter);
        binding.recyclerViewBookList3.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerViewBookList4.setAdapter(bookAdapter);
        binding.recyclerViewBookList4.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
    }

    private void initClickListener() {
        binding.editTextBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                        boardList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        binding.editTextBoard.setText(boardList.get(position));
                        bookListRequest.board = mBoardList.get(position).boardShortName;
                        binding.cardViewClass.setVisibility(View.VISIBLE);
                        binding.editTextClass.setText("");
                    }
                });
                popupWindow.show();
            }
        });

        binding.editTextClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = AppUtilities.showAnchoredPopup(getActivity(),
                        classList, R.layout.dropdown_menu_popup_item, R.id.textView, view);

                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        binding.editTextClass.setText("Class " + classList.get(position));
                        bookListRequest.sclass = mClassList.get(position).classId;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openBookListActivity();
                            }
                        }, 300);
                    }
                });
                popupWindow.show();
            }
        });


        binding.buttonViewAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookListActivity();
            }
        });

        binding.buttonViewAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookListActivity();
            }
        });

        binding.buttonViewAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookListActivity();
            }
        });
    }

    private void openBookListActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BookListActivity.BOOK_LIST_REQUEST_EXTRA, (Serializable) bookListRequest);
        startTargetActivity(BookListActivity.class, bundle);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void makeNetworkCall(Call call, String method) {
        if (AppUtilities.getInstance(getActivity()).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(call, method);
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {

    }

    @Override
    public void onFailure(String errorMessage) {
        showSnackBar(binding.rootLayout, errorMessage);
    }
}