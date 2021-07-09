package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateUserDetailsRequest implements Serializable {

    /**
     * username : testuser105
     * email : testuser105@test.com
     * mobile : 1051051051
     * sClass : X
     * boardName : WBBSE
     * instituteName : BMBHS
     */

    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("sClass")
    public String sClass;
    @SerializedName("boardName")
    public String boardName;
    @SerializedName("instituteName")
    public String instituteName;
}
