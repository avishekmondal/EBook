package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityListResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public List<ReturnData> returnData;
    @SerializedName("details")
    public String details;

    public static class ReturnData {
        @SerializedName("city_name")
        public String cityName;
    }
}
