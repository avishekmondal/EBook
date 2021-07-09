package com.app.ebook.models.book_details;

import com.google.gson.annotations.SerializedName;

public class BookContentRequest {

    /**
     * username : bikash_halder
     * book_id : 1
     */

    @SerializedName("username")
    public String username;
    @SerializedName("book_id")
    public String bookId;
}
