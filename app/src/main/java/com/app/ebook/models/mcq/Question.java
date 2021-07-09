
package com.app.ebook.models.mcq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("txt")
    @Expose
    public String txt;
    @SerializedName("img")
    @Expose
    public String img;

}
