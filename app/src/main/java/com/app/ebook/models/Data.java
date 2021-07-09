package com.app.ebook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("otpGeneratedTime")
    @Expose
    public String otpGeneratedTime;
    @SerializedName("otpVerification")
    @Expose
    public Boolean otpVerification;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("boardName")
    @Expose
    public String boardName;
    @SerializedName("instituteName")
    @Expose
    public String instituteName;
    @SerializedName("class")
    @Expose
    public String _class;
    @SerializedName("otp")
    @Expose
    public Integer otp;
    @SerializedName("joiningDate")
    @Expose
    public String joiningDate;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;
    @SerializedName("__v")
    @Expose
    public Integer v;
    @SerializedName("token")
    @Expose
    public String token;

}
