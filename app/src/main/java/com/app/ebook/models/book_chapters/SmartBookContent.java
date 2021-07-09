package com.app.ebook.models.book_chapters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SmartBookContent implements Serializable {

    /**
     * img :
     * txt : This branch takes a keen look at the behavior of light in various media.
     * title : ii) Geometrical Optics
     */

    @SerializedName("img")
    public String img;
    @SerializedName("txt")
    public String txt;
    @SerializedName("title")
    public String title;
}
