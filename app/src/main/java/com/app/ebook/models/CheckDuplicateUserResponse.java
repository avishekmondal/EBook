package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckDuplicateUserResponse {

    /**
     * ret_code : false
     * return_otp : 6571
     * return_data : {"email":["user with this email already exists."],"username":["user with this username already exists."],"password":["Ensure this field has at least 8 characters."],"mobile":["user with this mobile already exists."]}
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_otp")
    public String returnOtp;
    @SerializedName("return_data")
    public ReturnDataBean returnData;

    public static class ReturnDataBean {
        @SerializedName("username")
        public List<String> username;
        @SerializedName("email")
        public List<String> email;
        @SerializedName("mobile")
        public List<String> mobile;
        @SerializedName("password")
        public List<String> password;
    }

}
