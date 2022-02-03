package com.app.ebook.ui.activity;

import static com.app.ebook.network.UrlConstants.URL_CHAT;
import static com.app.ebook.network.UrlConstants.URL_POST_CHAT;
import static com.app.ebook.util.AppUtilities.showSnackBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityHelpAndSupportBinding;
import com.app.ebook.models.chat.ChatPostRequest;
import com.app.ebook.models.chat.ChatPostResponse;
import com.app.ebook.models.chat.ChatResponse;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.ui.adapter.ChatAdapter;
import com.app.ebook.util.AppUtilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class HelpAndSupportActivity extends BaseActivity implements RetrofitListener {

    private ActivityHelpAndSupportBinding binding;

    private RetroClient retroClient;

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_and_support);

        init();
        getChatList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        chatAdapter = new ChatAdapter(this);
        binding.recyclerViewChatList.setAdapter(chatAdapter);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickMailUs(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@readvi.in"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        //need this to prompts email client only
        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            AppUtilities.showToast(this, "There is no email client installed.");
        }
    }

    public void onClickKnowMore(View view) {
        startTargetActivity(FAQCategoryActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onClickZoom(View view) {
        if (binding.layoutTop.getVisibility() == View.VISIBLE) {
            binding.layoutTop.setVisibility(View.GONE);
        } else {
            binding.layoutTop.setVisibility(View.VISIBLE);
            binding.recyclerViewChatList.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }

    public void onClickSendMessage(View view) {
        if (binding.editTextMessage.getText().toString().length() > 0) {
            ChatPostRequest chatPostRequest = new ChatPostRequest();
            chatPostRequest.workspace = mUser.username;
            chatPostRequest.text = binding.editTextMessage.getText().toString();

            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).postChat(chatPostRequest),
                    URL_POST_CHAT);
        }
    }

    private void getChatList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();
            retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getChatList(),
                    URL_CHAT);
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        switch (method_name) {
            case URL_CHAT:
                ChatResponse chatResponse = (ChatResponse) response.body();
                if (chatResponse != null && chatResponse.retCode) {
                    ArrayList<ChatResponse.ReturnResponse> chatList = (ArrayList<ChatResponse.ReturnResponse>) chatResponse.returnResponse;
                    if (chatList.size() > 0) {
                        binding.layoutStartChat.setVisibility(View.GONE);
                        chatAdapter.setData(chatList);
                        binding.recyclerViewChatList.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                } else {
                    binding.layoutStartChat.setVisibility(View.VISIBLE);
                }
                break;
            case URL_POST_CHAT:
                ChatPostResponse chatPostResponse = (ChatPostResponse) response.body();
                if (chatPostResponse != null && chatPostResponse.retCode) {
                    binding.layoutStartChat.setVisibility(View.GONE);
                    binding.editTextMessage.setText("");
                    chatAdapter.setData(chatPostResponse.returnResponse);
                    binding.recyclerViewChatList.scrollToPosition(chatAdapter.getItemCount() - 1);
                } else {
                    showSnackBar(binding.rootLayout, chatPostResponse.details);
                }
                break;
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}