package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class UserDetailsResponse {

    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyMSwidXNlcm5hbWUiOiJhdmkxNjE5MTAxMDcwNTQwIiwiZXhwIjoxNjIxNzU5NjQ0fQ.dLDN2M8qRrY1jkS-zOZYxkKRpxwQ9wNnoqmwdDUrVuc
     * username : avi1619101070540
     * email : avi11@yopmail.com
     * full_name : Avishek Mondal
     * exp : 2021-05-23T08:47:24.943127
     * fname : Avishek
     * lname : Mondal
     * mobile : 6290295929
     * sClass : XII
     * boardName : WBCHSE
     * state : NA
     * city : NA
     * instituteName : BMBHS
     */

    @SerializedName("token")
    public String token;
    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("full_name")
    public String fullName;
    @SerializedName("exp")
    public String exp;
    @SerializedName("fname")
    public String fname;
    @SerializedName("lname")
    public String lname;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("sClass")
    public String sClass;
    @SerializedName("boardName")
    public String boardName;
    @SerializedName("state")
    public String state;
    @SerializedName("city")
    public String city;
    @SerializedName("instituteName")
    public String instituteName;
}
