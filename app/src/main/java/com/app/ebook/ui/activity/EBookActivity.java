package com.app.ebook.ui.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityEBookBinding;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.util.WheelView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EBookActivity extends BaseActivity {

    public static final String BOOK_DETAILS_EXTRA = "BookDetailsExtra";

    private ActivityEBookBinding binding;

    private BookListResponse.ReturnResponseBean returnResponseBean = new BookListResponse.ReturnResponseBean();

    private int totalPage, currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_e_book);

        init();
        initClickListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        if (getIntent().hasExtra(BOOK_DETAILS_EXTRA))
            returnResponseBean = (BookListResponse.ReturnResponseBean) getIntent().getSerializableExtra(BOOK_DETAILS_EXTRA);

        binding.textViewBook.setText(returnResponseBean.bookName);
        new PDFStream().execute(returnResponseBean.attachmentFile);
        //new PDFStream().execute(BASE_URL2 + "/media/attachments/Science_Experiments_for_Kids__PDFDrive__49O463v.pdf");
    }

    private void initClickListener() {
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pdfView.jumpTo(currentPage + 1);
            }
        });

        binding.buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pdfView.jumpTo(currentPage - 1);
            }
        });

        binding.buttonPageNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> pageList = new ArrayList<>();
                for (int i = 1; i <= totalPage; i++) {
                    pageList.add(String.valueOf(i));
                }

                if (pageList.size() > 0) {
                    View customView = LayoutInflater.from(EBookActivity.this).inflate(R.layout.wheel_view_item, null);
                    WheelView wheelView = customView.findViewById(R.id.wheelView);
                    wheelView.setItems(pageList);
                    wheelView.setOffset(1);
                    wheelView.setSeletion(1);
                    wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                        @Override
                        public void onSelected(int selectedIndex, String item) {
                            currentPage = selectedIndex;
                        }
                    });

                    new AlertDialog.Builder(EBookActivity.this)
                            .setTitle("Pick A Page")
                            .setView(customView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    binding.pdfView.jumpTo(currentPage - 1);
                                }
                            })
                            .show()
                            .getWindow().setLayout((int) (getWindowManager().getDefaultDisplay().getWidth() * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        });
    }

    public class PDFStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.showProgressDialog();
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL uri = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            mProgressDialog.hideProgressDialog();
            binding.pdfView.fromStream(inputStream)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFling(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null/*new DefaultScrollHandle(getActivity())*/)
                    .enableAntialiasing(true)
                    .spacing(0)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            totalPage = pageCount;
                            currentPage = page;
                            binding.buttonPageNo.setText(String.valueOf(currentPage + 1));
                        }
                    })
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages) {
                            binding.pdfView.fitToWidth(binding.pdfView.getCurrentPage());
                        }
                    })
                    .load();
        }
    }
}