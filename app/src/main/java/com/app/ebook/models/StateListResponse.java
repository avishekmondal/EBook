package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class StateListResponse {

    @SerializedName("state_id")
    public String stateId;
    @SerializedName("state_name")
    public String stateName;
}
