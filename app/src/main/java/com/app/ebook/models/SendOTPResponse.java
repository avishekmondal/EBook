package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class SendOTPResponse {

    /**
     * ret_code : false
     * return_data : 1234
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public String returnData;
}
