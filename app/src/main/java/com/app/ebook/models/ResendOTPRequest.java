package com.app.ebook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendOTPRequest {
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("email")
    @Expose
    public String email;
}
