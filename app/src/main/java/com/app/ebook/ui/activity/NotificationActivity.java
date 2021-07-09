package com.app.ebook.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityNotificationListBinding;
import com.app.ebook.ui.adapter.NotificationListAdapter;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_list);
//        Toolbar toolbar = findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NotificationListAdapter notificationListAdapter = new NotificationListAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(notificationListAdapter);

    }
}