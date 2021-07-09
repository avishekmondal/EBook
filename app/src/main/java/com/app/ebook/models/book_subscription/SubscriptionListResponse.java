
package com.app.ebook.models.book_subscription;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionListResponse {

    /**
     * ret_code : true
     * return_data : [{"id":7,"user_id":"avi1619101070540","product_id_id":1,"book_id_id":3,"start_date":"2021-05-28","end_date":"2024-05-12","status":"active","plan":"Basic","price":"0","product_type":"ebook","book_name":"Abstract Algebra","subject":"Algebra","days_left":"1071 days left"},{"id":18,"user_id":"avi1619101070540","product_id_id":2,"book_id_id":3,"start_date":"2021-05-29","end_date":"2022-11-20","status":"active","plan":"Standard","price":"300","product_type":"smart_coaching","book_name":"Abstract Algebra","subject":"Algebra","days_left":"532 days left"},{"id":2,"user_id":"avi1619101070540","product_id_id":3,"book_id_id":3,"start_date":"2021-05-27","end_date":"2025-05-06","status":"active","plan":"Platinum","price":"500","product_type":"smart_coaching","book_name":"Abstract Algebra","subject":"Algebra","days_left":"1430 days left"},{"id":19,"user_id":"avi1619101070540","product_id_id":11,"book_id_id":4,"start_date":"2021-06-03","end_date":"2021-11-30","status":"active","plan":"Standard","price":"500","product_type":"smart_coaching","book_name":"Introductory Physics","subject":"Physics","days_left":"177 days left"}]
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public List<ReturnDataBean> returnData;

    public static class ReturnDataBean {
        /**
         * id : 7
         * user_id : avi1619101070540
         * product_id_id : 1
         * book_id_id : 3
         * start_date : 2021-05-28
         * end_date : 2024-05-12
         * status : active
         * plan : Basic
         * price : 0
         * product_type : ebook
         * book_name : Abstract Algebra
         * subject : Algebra
         * days_left : 1071 days left
         */

        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("cover_photo")
        public String coverPhoto;
        @SerializedName("product_id_id")
        public String productIdId;
        @SerializedName("book_id_id")
        public String bookIdId;
        @SerializedName("start_date")
        public String startDate;
        @SerializedName("end_date")
        public String endDate;
        @SerializedName("status")
        public String status;
        @SerializedName("plan")
        public String plan;
        @SerializedName("price")
        public String price;
        @SerializedName("product_type")
        public String productType;
        @SerializedName("book_name")
        public String bookName;
        @SerializedName("subject")
        public String subject;
        @SerializedName("days_left")
        public String daysLeft;
    }
}