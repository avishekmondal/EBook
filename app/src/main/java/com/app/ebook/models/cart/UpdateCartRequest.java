package com.app.ebook.models.cart;

import com.google.gson.annotations.SerializedName;

public class UpdateCartRequest {

    /**
     * user_id : avi1619101070540
     * product_id : 3
     */

    @SerializedName("user_id")
    public String userId;
    @SerializedName("product_id")
    public String productId;
}
