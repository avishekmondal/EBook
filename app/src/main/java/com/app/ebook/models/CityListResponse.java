package com.app.ebook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityListResponse {
    @SerializedName("responseCode")
    @Expose
    public Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    public String responseMessage;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("list")
        @Expose
        public java.util.List<List> list = null;
        @SerializedName("pageLimit")
        @Expose
        public Integer pageLimit;
        @SerializedName("page")
        @Expose
        public Integer page;
        @SerializedName("totalCount")
        @Expose
        public Integer totalCount;

    }

    public class List {

        @SerializedName("_id")
        @Expose
        public String id;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("createdAt")
        @Expose
        public String createdAt;
        @SerializedName("updatedAt")
        @Expose
        public String updatedAt;
        @SerializedName("__v")
        @Expose
        public Integer v;
        @SerializedName("Sr")
        @Expose
        public Integer sr;
    }
}
