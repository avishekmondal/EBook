package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FAQResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_response")
    public List<ReturnResponse> returnResponse;
    @SerializedName("message")
    public String message;

    public static class ReturnResponse implements Serializable {

        @SerializedName("category")
        public String category;
        @SerializedName("faqs")
        public List<Faqs> faqs;

        public static class Faqs implements Serializable{
            @SerializedName("question")
            public String question;
            @SerializedName("answer")
            public String answer;
        }
    }
}
