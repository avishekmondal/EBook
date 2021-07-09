
package com.app.ebook.models.wish_list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddToWishListResponse {

    /**
     * ret_code : true
     * message : {"username":"bikash_halder","wishlist":["1"]}
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("message")
    public MessageBean message;

    public static class MessageBean {
        /**
         * username : bikash_halder
         * wishlist : ["1"]
         */

        @SerializedName("username")
        public String username;
        @SerializedName("wishlist")
        public List<String> wishlist;
    }
}