package com.app.ebook.ui.fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentEBookBinding;
import com.app.ebook.util.WheelView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EBookFragment extends BaseFragment {

    private FragmentEBookBinding binding;

    private int totalPage, currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_e_book, container, false);

        init();
        initClickListener();

        return binding.getRoot();
    }

    private void init() {
        new PDFStream().execute(mBookDetails.attachmentFile);
    }

    private void initClickListener() {
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
                    View customView = LayoutInflater.from(getActivity()).inflate(R.layout.wheel_view_item, null);
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

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Pick A Page")
                            .setView(customView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    binding.pdfView.jumpTo(currentPage - 1);
                                }
                            })
                            .show()
                            .getWindow().setLayout((int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        });
    }

    public class PDFStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    .enableAntialiasing(false)
                    .spacing(0)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            totalPage = pageCount;
                            currentPage = page;
                            binding.buttonPageNo.setText(String.valueOf(currentPage + 1));
                        }
                    })
                    .load();
        }
    }
}