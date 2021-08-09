package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckDuplicateProfileRequest implements Serializable {

    @SerializedName("username")
    public String username;
}
