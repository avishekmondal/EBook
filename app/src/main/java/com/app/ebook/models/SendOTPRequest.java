package com.app.ebook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOTPRequest {
    @SerializedName("email")
    @Expose
    public String email;
}
