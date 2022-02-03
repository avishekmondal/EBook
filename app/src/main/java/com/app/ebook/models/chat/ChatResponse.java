package com.app.ebook.models.chat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_response")
    public List<ReturnResponse> returnResponse;

    public static class ReturnResponse implements Serializable {

        @SerializedName("ques_id")
        public int quesId;
        @SerializedName("workspace")
        public String workspace;
        @SerializedName("username")
        public String username;
        @SerializedName("time")
        public String time;
        @SerializedName("text")
        public String text;
    }
}
