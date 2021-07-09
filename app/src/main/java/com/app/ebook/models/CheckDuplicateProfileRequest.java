package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckDuplicateProfileRequest implements Serializable {

    /**
     * username : testuser100
     * email : testuser100@test.com
     * mobile : 1234567890
     */

    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
}
