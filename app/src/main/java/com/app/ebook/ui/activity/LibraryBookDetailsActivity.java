package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityLibraryBookDetailsBinding;
import com.app.ebook.ui.adapter.BookDetailsAdapter;

public class LibraryBookDetailsActivity extends BaseActivity {

    private ActivityLibraryBookDetailsBinding binding;

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

    private void init() {
        binding.textViewTitle.setText(mBookDetails.bookName);

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
}