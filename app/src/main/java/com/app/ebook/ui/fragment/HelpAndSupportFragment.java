package com.app.ebook.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ebook.R;
import com.app.ebook.databinding.FragmentHelpAndSupportBinding;
import com.app.ebook.ui.adapter.HelpListAdapter;


public class HelpAndSupportFragment extends Fragment {
    private FragmentHelpAndSupportBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_help_and_support, container, false);
        HelpListAdapter helpListAdapter = new HelpListAdapter(getActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(helpListAdapter);

        return binding.getRoot();
    }
}