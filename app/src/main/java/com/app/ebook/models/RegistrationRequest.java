package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegistrationRequest implements Serializable {

    @SerializedName("name")
    public String name;
    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("password")
    public String password;
}
