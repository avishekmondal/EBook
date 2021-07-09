
package com.app.ebook.models.subjective;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Question implements Serializable {

    @SerializedName("txt")
    @Expose
    public String txt;
    @SerializedName("img")
    @Expose
    public String img;

}
