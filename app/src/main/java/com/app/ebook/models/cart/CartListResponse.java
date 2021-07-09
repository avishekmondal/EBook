package com.app.ebook.models.cart;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CartListResponse {

    /**
     * ret_code : true
     * return_data : [{"user_id":"avi1619101070540","product_id":3,"added_date":"2021-05-29T12:37:49.036616Z","book_id":3,"plan":"Platinum","price":"500","duration":"12","details":"12 months Ebook, 12 months Smart coaching","product_type":"smart_coaching","book_category":"Academic Book","book_name":"Abstract Algebra","board":"WBCHSE","sclass":"12","subject":"Algebra","book_type":"Text Book","publisher_name":"Government Book","author_name":"Test Author","cover_photo":"/media/attachments/science-224-904154.png","content_sequence":null,"attachment_file":"/media/attachments/AbstractAlgebra.pdf","is_wishlist":false,"is_subscribed":false}]
     * total_price : 500
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("total_price")
    public int totalPrice;
    @SerializedName("return_data")
    public List<ReturnDataBean> returnData;

    public static class ReturnDataBean implements Serializable {
        /**
         * user_id : avi1619101070540
         * product_id : 3
         * added_date : 2021-05-29T12:37:49.036616Z
         * book_id : 3
         * plan : Platinum
         * price : 500
         * duration : 12
         * details : 12 months Ebook, 12 months Smart coaching
         * product_type : smart_coaching
         * book_category : Academic Book
         * book_name : Abstract Algebra
         * board : WBCHSE
         * sclass : 12
         * subject : Algebra
         * book_type : Text Book
         * publisher_name : Government Book
         * author_name : Test Author
         * cover_photo : /media/attachments/science-224-904154.png
         * content_sequence : null
         * attachment_file : /media/attachments/AbstractAlgebra.pdf
         * is_wishlist : false
         * is_subscribed : false
         */

        @SerializedName("user_id")
        public String userId;
        @SerializedName("product_id")
        public String productId;
        @SerializedName("added_date")
        public String addedDate;
        @SerializedName("book_id")
        public String bookId;
        @SerializedName("plan")
        public String plan;
        @SerializedName("price")
        public String price;
        @SerializedName("duration")
        public String duration;
        @SerializedName("details")
        public String details;
        @SerializedName("product_type")
        public String productType;
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
        @SerializedName("content_sequence")
        public Object contentSequence;
        @SerializedName("attachment_file")
        public String attachmentFile;
        @SerializedName("is_wishlist")
        public boolean isWishlist;
        @SerializedName("is_subscribed")
        public boolean isSubscribed;
        @SerializedName("is_checked")
        public boolean isChecked;
    }
}
