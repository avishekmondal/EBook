package com.app.ebook.models.exam_prep_chapters;

import com.google.gson.annotations.SerializedName;

public class ExamPrepChapterListRequest {

    @SerializedName("book_id")
    public String bookId;
    @SerializedName("category")
    public String category;
}
