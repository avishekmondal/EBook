
package com.app.ebook.models.subjective;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReturnData implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("marks")
    @Expose
    public Integer marks;
    @SerializedName("answer")
    @Expose
    public List<Answer> answer = null;
    @SerializedName("chapter")
    @Expose
    public String chapter;
    @SerializedName("question")
    @Expose
    public Question question;

}
