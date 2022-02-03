package com.app.ebook.models.chat;

import com.google.gson.annotations.SerializedName;

public class ChatPostResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_response")
    public ChatResponse.ReturnResponse returnResponse;
    @SerializedName("details")
    public String details;
}
