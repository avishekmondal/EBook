package com.app.ebook.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivitySmartBookBinding;
import com.app.ebook.models.book_chapters.Heading;
import com.app.ebook.models.book_chapters.SmartBookContent;
import com.app.ebook.util.AppUtilities;

import java.util.List;

import static com.app.ebook.util.Constants.IS_SUBSCRIBED;

public class SmartBookActivity extends BaseActivity {

    public static final String SMART_BOOK_REQUEST_EXTRA = "SmartBookRequestExtra";

    private ActivitySmartBookBinding binding;

    private Heading heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_smart_book);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        heading = (Heading) getIntent().getSerializableExtra("SmartBookRequestExtra");

        binding.textViewHeading.setText(heading.headingName);
        binding.textViewSubHeading.setText(heading.subheading);

        List<SmartBookContent> smartBookContentList = heading.smartBookContents;
        for (int i = 0; i < smartBookContentList.size(); i++) {
            // Title
            if (smartBookContentList.get(i).title != null && !TextUtils.isEmpty(smartBookContentList.get(i).title)) {
                TextView textView = new TextView(this);
                textView.setText(binding.layoutContent.getChildCount() == 0  ? smartBookContentList.get(i).title :
                        "\n" + smartBookContentList.get(i).title);
                textView.setTextColor(getColor(R.color.black));
                textView.setTextSize(17);
                binding.layoutContent.addView(textView);
            }
            // Text
            if (smartBookContentList.get(i).txt != null && !TextUtils.isEmpty(smartBookContentList.get(i).txt)) {
                TextView textView = new TextView(this);
                textView.setText(smartBookContentList.get(i).txt);
                textView.setTextColor(getColor(R.color.black_646464));
                textView.setTextSize(14);
                textView.setLineSpacing(getResources().getDimension(R.dimen._1sdp), 1.2f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 15,0, 0);
                textView.setLayoutParams(layoutParams);
                binding.layoutContent.addView(textView);
            }
            // Image
            if (smartBookContentList.get(i).img != null && !TextUtils.isEmpty(smartBookContentList.get(i).img)) {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 300);
                layoutParams.setMargins(0, 25,0, 15);
                layoutParams.gravity = Gravity.CENTER;
                imageView.setLayoutParams(layoutParams);
                AppUtilities.loadImage(this, imageView, smartBookContentList.get(i).img);
                binding.layoutContent.addView(imageView);
            }
        }

        if (!mSessionManager.getBooleanSession(IS_SUBSCRIBED)) {
            binding.layoutSubscribeNow.setVisibility(View.VISIBLE);
        } else {
            binding.layoutSubscribeNow.setVisibility(View.GONE);
        }
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickSubscribe(View view) {
        goToSubscriptionPlanActivity();
    }

}