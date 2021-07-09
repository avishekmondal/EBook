
package com.app.ebook.models.book_details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookContentResponse {

    /**
     * ret_code : true
     * return_response : {"book_id":1,"book_category":"Academic Book","book_name":"Blossoms","board":"WBBSE","sclass":"8","subject":"English","book_type":"Text Book","publisher_name":"Government Book","author_name":"Test Author","cover_photo":"/media/attachments/document-page0.pdf","book_price":"500","content_sequence":null,"attachment_file":"/media/attachments/book_1.PDF"}
     * is_subscribed : false
     * is_wishlist : false
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_response")
    public ReturnResponseBean returnResponse;
    @SerializedName("is_subscribed")
    public boolean isSubscribed;
    @SerializedName("is_wishlist")
    public boolean isWishlist;

    public static class ReturnResponseBean implements Serializable {
        /**
         * book_id : 1
         * book_category : Academic Book
         * book_name : Blossoms
         * board : WBBSE
         * sclass : 8
         * subject : English
         * book_type : Text Book
         * publisher_name : Government Book
         * author_name : Test Author
         * cover_photo : /media/attachments/document-page0.pdf
         * book_price : 500
         * content_sequence : null
         * attachment_file : /media/attachments/book_1.PDF
         */

        @SerializedName("book_id")
        public int bookId;
        @SerializedName("book_category")
        public String bookCategory;
        @SerializedName("book_name")
        public String bookName;
        @SerializedName("board")
        public String board;
        @SerializedName("sclass")
        public String sclass;
        @SerializedName("subject")
        public String subject;
        @SerializedName("book_type")
        public String bookType;
        @SerializedName("publisher_name")
        public String publisherName;
        @SerializedName("author_name")
        public String authorName;
        @SerializedName("cover_photo")
        public String coverPhoto;
        @SerializedName("book_price")
        public String bookPrice;
        @SerializedName("content_sequence")
        public Object contentSequence;
        @SerializedName("attachment_file")
        public String attachmentFile;
    }
}
