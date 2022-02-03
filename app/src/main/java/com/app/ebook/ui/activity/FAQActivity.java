package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityFaqBinding;
import com.app.ebook.models.FAQResponse;
import com.app.ebook.ui.adapter.FAQAdapter;

public class FAQActivity extends BaseActivity {

    public static final String FAQ_EXTRA = "FAQExtra";

    private ActivityFaqBinding binding;

    private FAQResponse.ReturnResponse faqResponse;
    private FAQAdapter faqAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        if (getIntent().hasExtra(FAQ_EXTRA))
            faqResponse = (FAQResponse.ReturnResponse) getIntent().getSerializableExtra(FAQ_EXTRA);

        binding.textViewTitle.setText(faqResponse.category + " FAQs");
        faqAdapter = new FAQAdapter(this, faqResponse.faqs);
        binding.recyclerViewFaqList.setAdapter(faqAdapter);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}
