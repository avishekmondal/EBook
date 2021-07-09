package com.app.ebook.models.book_chapters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Videos implements Serializable {

    @SerializedName("video_id")
    public String videoId;
    @SerializedName("video_file")
    public String videoFile;
}
