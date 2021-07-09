package com.app.ebook.models.cart;

import com.google.gson.annotations.SerializedName;

public class UpdateCartResponse {

    /**
     * ret_code : true
     * message : Product successfully added to cart.
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("message")
    public String message;
}
