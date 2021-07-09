
package com.app.ebook.models.mcq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnData {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("chapter")
    @Expose
    public String chapter;
    @SerializedName("options")
    @Expose
    public List<Option> options = null;
    @SerializedName("question")
    @Expose
    public Question question;
    @SerializedName("correct_option")
    @Expose
    public Integer correctOption;

}
