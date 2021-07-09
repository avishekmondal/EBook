
package com.app.ebook.models.book_list;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookListRequest implements Serializable {

    /**
     * username : bikash_halder
     * sclass : 8
     * board : WBBSE
     */

    @SerializedName("username")
    public String username;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("sclass")
    public String sclass;
    @SerializedName("board")
    public String board;
}