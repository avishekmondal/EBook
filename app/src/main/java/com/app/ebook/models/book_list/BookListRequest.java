
package com.app.ebook.models.book_list;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookListRequest implements Serializable {

    /**
     * sclass : 8
     * board : WBBSE
     */

    @SerializedName("sclass")
    public String sclass;
    @SerializedName("board")
    public String board;
}