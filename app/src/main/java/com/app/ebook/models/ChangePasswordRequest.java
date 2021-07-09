package com.app.ebook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest {
    @SerializedName("oldPassword")
    @Expose
    public String oldPassword;
    @SerializedName("newPassword")
    @Expose
    public String newPassword;
}
