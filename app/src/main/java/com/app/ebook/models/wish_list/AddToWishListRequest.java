
package com.app.ebook.models.wish_list;

import com.google.gson.annotations.SerializedName;

public class AddToWishListRequest {

    /**
     * username : bikash_halder
     * wishlist : 1
     */

    @SerializedName("username")
    public String username;
    @SerializedName("wishlist")
    public String wishlist;
}