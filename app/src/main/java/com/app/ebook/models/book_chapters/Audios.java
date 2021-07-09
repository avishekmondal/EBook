package com.app.ebook.models.book_chapters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Audios implements Serializable {

    @SerializedName("audio_id")
    public String audioId;
    @SerializedName("audio_file")
    public String audioFile;
}
