package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckDuplicateUserRequest implements Serializable {

    /**
     * username : testuser100
     * email : testuser100@test.com
     * mobile : 1234567890
     * password : Test@123
     */

    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("password")
    public String password;
}
