package com.app.ebook.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.app.ebook.R;

public class ProgressDialog {

    private Dialog pDialog;
    private TextView tv_message;

    public ProgressDialog(Activity activity, String message, Boolean cancelable) {
        pDialog = new Dialog(activity);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.loading_avl);
        pDialog.setCancelable(cancelable);
        pDialog.setCanceledOnTouchOutside(cancelable);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message = (TextView) pDialog.findViewById(R.id.tv_message);
        tv_message.setText(message);
    }

    public ProgressDialog(Context context, String message, Boolean cancelable) {
        pDialog = new Dialog(context);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.loading_avl);
        pDialog.setCancelable(cancelable);
        pDialog.setCanceledOnTouchOutside(cancelable);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message = pDialog.findViewById(R.id.tv_message);
        tv_message.setText(message);
    }

    public void setTv_message(String message) {
        tv_message.setText(message);
    }

    public void showProgressDialog() {
        try {
            if (pDialog != null) {
                if (!pDialog.isShowing())
                    pDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        try {
            if (pDialog != null) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
