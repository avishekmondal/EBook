package com.app.ebook.models.chat;

import com.google.gson.annotations.SerializedName;

public class ChatPostRequest {

    @SerializedName("workspace")
    public String workspace;
    @SerializedName("text")
    public String text;
}
