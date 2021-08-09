package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateUserDetailsRequest implements Serializable {

    @SerializedName("username")
    public String username;
    @SerializedName("sClass")
    public String sClass;
    @SerializedName("boardName")
    public String boardName;
    @SerializedName("instituteName")
    public String instituteName;
}
