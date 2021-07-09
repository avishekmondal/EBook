package com.app.ebook.models;

import com.google.gson.annotations.SerializedName;

public class BoardListResponse {

        /**
         * board_id : 1
         * board_full_name : Central Board of Secondary Education
         * board_short_name : CBSE
         */

        @SerializedName("board_id")
        public String boardId;
        @SerializedName("board_full_name")
        public String boardFullName;
        @SerializedName("board_short_name")
        public String boardShortName;

}
