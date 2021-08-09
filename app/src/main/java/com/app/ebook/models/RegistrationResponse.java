package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public ReturnDataBean returnData;
    @SerializedName("message")
    public String message;

    public static class ReturnDataBean {

        @SerializedName("token")
        public String token;
        @SerializedName("username")
        public String username;
        @SerializedName("email")
        public String email;
        @SerializedName("name")
        public String name;
        @SerializedName("exp")
        public String exp;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("sClass")
        public String sClass;
        @SerializedName("boardName")
        public String boardName;
        @SerializedName("state")
        public String state;
        @SerializedName("city")
        public String city;
        @SerializedName("instituteName")
        public String instituteName;
    }
}
