package com.app.ebook.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityIntroSliderBinding;
import com.app.ebook.ui.adapter.IntroSliderPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class IntroSliderActivity extends FragmentActivity {

    PagerAdapter pagerAdapter;
    private ActivityIntroSliderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_slider);
        pagerAdapter = new IntroSliderPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.getTabAt(position).select();
                if (position == 2) {
                    binding.buttonFinish.setVisibility(View.VISIBLE);
                    binding.fab.setVisibility(View.GONE);
                } else {
                    binding.buttonFinish.setVisibility(View.INVISIBLE);
                    binding.fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager));
    }

    @Override
    public void onBackPressed() {
        if (binding.viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
        }
    }

    public void fabButtonClicked(View view) {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
    }

    public void openLoginActivity(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}