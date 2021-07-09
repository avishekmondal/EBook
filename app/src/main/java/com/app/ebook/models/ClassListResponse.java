package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class ClassListResponse {

    /**
     * class_id : 1
     * class_name : I
     */

    @SerializedName("class_id")
    public String classId;
    @SerializedName("class_name")
    public String className;
}
