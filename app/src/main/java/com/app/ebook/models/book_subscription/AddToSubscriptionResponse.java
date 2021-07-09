package com.app.ebook.models.book_subscription;

import com.google.gson.annotations.SerializedName;

public class AddToSubscriptionResponse {

    /**
     * ret_code : true
     * message : Product successfully subscribed.
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("message")
    public String message;
}
