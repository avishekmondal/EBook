
package com.app.ebook.models.book_list;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookListResponse {

    /**
     * ret_code : true
     * return_response : [{"book_id":1,"book_category":"Academic Book","book_name":"Blossoms","board":"WBBSE","sclass":"8","subject":"English","book_type":"Text Book","publisher_name":"Government Book","author_name":"Test Author","cover_photo":"/media/attachments/document-page0.pdf","book_price":"500","content_sequence":null,"attachment_file":"/media/attachments/book_1.PDF","is_wishlist":false},{"book_id":2,"book_category":"Academic Book","book_name":"Test book","board":"WBBSE","sclass":"8","subject":"English","book_type":"Text Book","publisher_name":"Government Book","author_name":"Test Author","cover_photo":"/media/attachments/1610875798960.jpg","book_price":"300","content_sequence":null,"attachment_file":"/media/attachments/Science_Experiments_for_Kids__PDFDrive__49O463v.pdf","is_wishlist":true},{"book_id":3,"book_category":"Academic Book","book_name":"Abstract Algebra","board":"WBCHSE","sclass":"12","subject":"Algebra","book_type":"Text Book","publisher_name":"Government Book","author_name":"Test Author","cover_photo":"/media/attachments/science-224-904154.png","book_price":"500","content_sequence":null,"attachment_file":"/media/attachments/AbstractAlgebra.pdf","is_wishlist":false},{"book_id":4,"book_category":"Academic Book","book_name":"Introductory Physics","board":"WBCHSE","sclass":"12","subject":"Physics","book_type":"Text Book","publisher_name":"Government Book","author_name":"Test Author","cover_photo":"/media/attachments/001_Q3_3.jpeg","book_price":"500","content_sequence":null,"attachment_file":"/media/attachments/physics_book.pdf","is_wishlist":false}]
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_response")
    public List<ReturnResponseBean> returnResponse;

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
         * is_wishlist : false
         * is_subscribed : false
         * end_date : 2022-12-31
         * days_left : 365 days left
         */

        @SerializedName("book_id")
        public String bookId;
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
        public String contentSequence;
        @SerializedName("attachment_file")
        public String attachmentFile;
        @SerializedName("is_wishlist")
        public boolean isWishlist;
        @SerializedName("is_subscribed")
        public boolean isSubscribed;
        @SerializedName("end_date")
        public String endDate;
        @SerializedName("days_left")
        public String daysLeft;
    }
}