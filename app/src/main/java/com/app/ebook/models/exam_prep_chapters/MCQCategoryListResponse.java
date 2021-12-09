
package com.app.ebook.models.exam_prep_chapters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MCQCategoryListResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_response")
    public List<String> returnResponse;

}
