package com.app.ebook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyOTPRequest implements Serializable {
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("otp")
    @Expose
    public String otp;
}
