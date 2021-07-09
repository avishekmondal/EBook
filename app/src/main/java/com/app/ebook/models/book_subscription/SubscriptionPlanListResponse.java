package com.app.ebook.models.book_subscription;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionPlanListResponse {

    /**
     * ret_code : true
     * return_data : [{"product_id":1,"book_id":3,"plan":"Basic","price":"0","duration":"12","details":"12 months Ebook","product_type":"ebook"},{"product_id":2,"book_id":3,"plan":"Standard","price":"300","duration":"6","details":"12 months Ebook, 6 months Smart coaching","product_type":"smart_coaching"},{"product_id":3,"book_id":3,"plan":"Platinum","price":"500","duration":"12","details":"12 months Ebook, 12 months Smart coaching","product_type":"smart_coaching"}]
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public List<ReturnDataBean> returnData;

    public static class ReturnDataBean {
        /**
         * product_id : 1
         * book_id : 3
         * plan : Basic
         * price : 0
         * duration : 12
         * details : 12 months Ebook
         * product_type : ebook
         */

        @SerializedName("product_id")
        public String productId;
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
    }
}
