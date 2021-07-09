
package com.app.ebook.models.subjective;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectiveListResponse {

    @SerializedName("ret_code")
    @Expose
    public Boolean retCode;
    @SerializedName("return_data")
    @Expose
    public List<ReturnData> returnData = null;

}
