
package com.app.ebook.models.book_chapters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReturnData implements Serializable {

    @SerializedName("chapter_id")
    public String chapterId;
    @SerializedName("chapter_name")
    public String chapterName;
    @SerializedName("heading")
    public List<Heading> heading;

}
