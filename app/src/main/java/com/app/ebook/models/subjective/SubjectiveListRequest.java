package com.app.ebook.models.subjective;

import com.google.gson.annotations.SerializedName;

public class SubjectiveListRequest {

    /**
     * book_id : 1
     * chapter : Chapter2
     */

    @SerializedName("book_id")
    public String bookId;
    @SerializedName("chapter")
    public String chapter;
}
