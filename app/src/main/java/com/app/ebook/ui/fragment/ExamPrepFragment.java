package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentExamPrepBinding;
import com.app.ebook.ui.activity.ExamPrepChapterActivity;

import static com.app.ebook.util.Constants.KEY;

public class ExamPrepFragment extends BaseFragment {

    private FragmentExamPrepBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exam_prep, container, false);

        initClickListener();

        return binding.getRoot();
    }

    private void initClickListener() {
        binding.layoutMCQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextActivity(getString(R.string.menu_mcq));
            }
        });

        binding.layoutSubjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextActivity(getString(R.string.menu_subjective));
            }
        });
    }

    private void goToNextActivity(String menu) {
        mSessionManager.setSession(KEY, menu);
        startTargetActivity(ExamPrepChapterActivity.class);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}