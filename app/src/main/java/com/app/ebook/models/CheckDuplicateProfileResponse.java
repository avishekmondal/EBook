package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class CheckDuplicateProfileResponse {

    /**
     * ret_code : false
     * return_data : Email or mobile already exist.
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public String returnData;
}
