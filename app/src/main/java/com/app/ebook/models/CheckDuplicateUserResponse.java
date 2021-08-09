package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckDuplicateUserResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public String returnData;
    @SerializedName("details")
    public DetailsBean details;
    @SerializedName("return_otp")
    public String returnOtp;

    public static class DetailsBean {
        @SerializedName("name")
        public List<String> name;
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
