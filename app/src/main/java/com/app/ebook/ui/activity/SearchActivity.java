package com.app.ebook.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_old);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, BookListActivity.class));
            }
        });

       /* String[] categories = {"Book No- 1", "Book No- 1", "Book No- 1", "Book No- 1", "Book No- 1", "Book No- 1", "Book No- 1", "Book No- 1"};
        for (String s : categories) {
            Chip chip = new Chip(this);
            chip.setElevation(10);
            chip.setText(s);
            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setCheckable(true);
            chip.setChipIconVisible(false);
            chip.setCheckedIconVisible(false);
            chip.setCloseIconVisible(false);
            binding.bookGroup.addView(chip);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}