package com.app.ebook.models.mcq;

import com.google.gson.annotations.SerializedName;

public class MCQListRequest {

    /**
     * book_id : 1
     * chapter : Chapter2
     */

    @SerializedName("book_id")
    public String bookId;
    @SerializedName("chapter")
    public String chapter;
}
