package com.app.ebook.models.book_subscription;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddToSubscriptionRequest {

    /**
     * user_id : avi1619101070540
     * product_ids : ["3"]
     */

    @SerializedName("user_id")
    public String userId;
    @SerializedName("product_ids")
    public List<String> productIds;
}
