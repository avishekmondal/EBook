package com.app.ebook.models.book_chapters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Heading implements Serializable {

    @SerializedName("heading_id")
    public String headingId;
    @SerializedName("chapter_id_id")
    public String chapterIdId;
    @SerializedName("heading_name")
    public String headingName;
    @SerializedName("subheading")
    public String subheading;
    @SerializedName("audios")
    public List<Audios> audios;
    @SerializedName("videos")
    public List<Videos> videos;

}
