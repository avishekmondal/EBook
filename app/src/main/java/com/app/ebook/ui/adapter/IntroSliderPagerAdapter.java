package com.app.ebook.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.ebook.ui.fragment.OnboardFragment1;
import com.app.ebook.ui.fragment.OnboardFragment2;
import com.app.ebook.ui.fragment.OnboardFragment3;

public class IntroSliderPagerAdapter extends FragmentStatePagerAdapter {

    public IntroSliderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new OnboardFragment1();
        } else if (position == 1) {
            return new OnboardFragment2();
        } else if (position == 2) {
            return new OnboardFragment3();
        } else return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
