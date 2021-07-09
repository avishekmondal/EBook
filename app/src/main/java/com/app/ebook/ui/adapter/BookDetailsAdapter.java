package com.app.ebook.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.ebook.ui.fragment.CoachingFragment;
import com.app.ebook.ui.fragment.EBookFragment;
import com.app.ebook.ui.fragment.ExamPrepFragment;

public class BookDetailsAdapter extends FragmentStatePagerAdapter {

    public BookDetailsAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EBookFragment();
        } else if (position == 1) {
            return new CoachingFragment();
        } else if (position == 2) {
            return new ExamPrepFragment();
        } else return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object != null) {
            return ((Fragment) object).getView() == view;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
