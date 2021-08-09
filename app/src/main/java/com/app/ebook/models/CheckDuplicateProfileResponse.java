package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class CheckDuplicateProfileResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public String returnData;
    @SerializedName("return_otp")
    public String returnOtp;
}
