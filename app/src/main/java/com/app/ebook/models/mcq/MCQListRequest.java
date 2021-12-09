package com.app.ebook.models.mcq;

import com.google.gson.annotations.SerializedName;

public class MCQListRequest {

    @SerializedName("book_id")
    public String bookId;
    @SerializedName("category")
    public String category;
    @SerializedName("chapter")
    public String chapter;
}
