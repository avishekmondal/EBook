
package com.app.ebook.models.book_chapters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookChapterListResponse {

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public List<ReturnData> returnData;
}
