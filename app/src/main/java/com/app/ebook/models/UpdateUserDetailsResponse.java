package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class UpdateUserDetailsResponse {

    /**
     * ret_code : true
     * return_data : {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MjEsImV4cCI6NDB9.4HZeqOfrgTZQD1UHq2bhQQI6Wn3hZKqKM0yiRuJ6xHE","username":"avi1619101070540","email":"avi11@yopmail.com","full_name":"Avishek Mondal","exp":"2021-05-24T12:35:40.066","fname":"Avishek","lname":"Mondal","mobile":"6290295929","sClass":"XII","boardName":"WBCHSE","state":"NA","city":"NA","instituteName":"BMBHS"}
     * message : Data updated succesfully.
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public ReturnDataBean returnData;
    @SerializedName("message")
    public String message;

    public static class ReturnDataBean {
        /**
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MjEsImV4cCI6NDB9.4HZeqOfrgTZQD1UHq2bhQQI6Wn3hZKqKM0yiRuJ6xHE
         * username : avi1619101070540
         * email : avi11@yopmail.com
         * full_name : Avishek Mondal
         * exp : 2021-05-24T12:35:40.066
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
}
