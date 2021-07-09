package com.app.ebook.models.book_chapters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoListResponse {

    /**
     * ret_code : true
     * return_data : [{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Sets and Functions","heading_id":2,"subheading":"Basic Logic and Set Notation","video_id":1,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Sets and Functions","heading_id":2,"subheading":"Basic Logic and Set Notation","video_id":2,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Sets and Functions","heading_id":2,"subheading":"Basic Logic and Set Notation","video_id":18,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Sets and Functions","heading_id":2,"subheading":"Basic Logic and Set Notation","video_id":23,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Sets and Functions","heading_id":2,"subheading":"Basic Logic and Set Notation","video_id":24,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Equivalence Relations","heading_id":3,"subheading":"Basic Logic and Set Notation","video_id":3,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Equivalence Relations","heading_id":3,"subheading":"Basic Logic and Set Notation","video_id":20,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"Equivalence Relations","heading_id":3,"subheading":"Basic Logic and Set Notation","video_id":28,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Introduction","chapter_id":1,"heading_name":"What is Algebra?","heading_id":1,"subheading":"Basic Logic and Set Notation","video_id":29,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Basic Observations","heading_id":4,"subheading":"Basic Logic and Set Notation","video_id":4,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Basic Observations","heading_id":4,"subheading":"Basic Logic and Set Notation","video_id":21,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Factorization and the Fundamental Theorem of Arithmetic","heading_id":5,"subheading":"Basic Logic and Set Notation","video_id":5,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Factorization and the Fundamental Theorem of Arithmetic","heading_id":5,"subheading":"Basic Logic and Set Notation","video_id":22,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Factorization and the Fundamental Theorem of Arithmetic","heading_id":5,"subheading":"Basic Logic and Set Notation","video_id":25,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Congruences","heading_id":6,"subheading":"Basic Logic and Set Notation","video_id":6,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Congruences","heading_id":6,"subheading":"Basic Logic and Set Notation","video_id":13,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"The Structure of + and × on Z","chapter_id":2,"heading_name":"Congruences","heading_id":6,"subheading":"Basic Logic and Set Notation","video_id":26,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Basic Definitions","heading_id":7,"subheading":"Basic Logic and Set Notation","video_id":7,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Basic Definitions","heading_id":7,"subheading":"Basic Logic and Set Notation","video_id":14,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Basic Definitions","heading_id":7,"subheading":"Basic Logic and Set Notation","video_id":30,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Subgroups, Cosets and Lagrangeâ\u20ac™s Theorem","heading_id":8,"subheading":"Basic Logic and Set Notation","video_id":8,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Subgroups, Cosets and Lagrangeâ\u20ac™s Theorem","heading_id":8,"subheading":"Basic Logic and Set Notation","video_id":15,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Subgroups, Cosets and Lagrangeâ\u20ac™s Theorem","heading_id":8,"subheading":"Basic Logic and Set Notation","video_id":27,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Finitely Generated Groups","heading_id":9,"subheading":"Basic Logic and Set Notation","video_id":9,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Finitely Generated Groups","heading_id":9,"subheading":"Basic Logic and Set Notation","video_id":16,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Permutation Groups and Group Actions","heading_id":10,"subheading":"Basic Logic and Set Notation","video_id":10,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"The Orbit-Stabiliser Theorem and Sylowâ\u20ac™s Theorem","heading_id":11,"subheading":"Basic Logic and Set Notation","video_id":11,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Finite Symmetric Groups","heading_id":12,"subheading":"Basic Logic and Set Notation","video_id":12,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Finite Symmetric Groups","heading_id":12,"subheading":"Basic Logic and Set Notation","video_id":17,"video_file":"https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"},{"chapter_name":"Groups","chapter_id":3,"heading_name":"Finite Symmetric Groups","heading_id":12,"subheading":"Basic Logic and Set Notation","video_id":19,"video_file":"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","video_name":"Test Video.mp4","video_size":"10 MB","video_cover":"SampleVideoCover.png","video_duration":"10 min"}]
     */

    @SerializedName("ret_code")
    public boolean retCode;
    @SerializedName("return_data")
    public List<ReturnDataBean> returnData;

    public static class ReturnDataBean {
        /**
         * chapter_name : Introduction
         * chapter_id : 1
         * heading_name : Sets and Functions
         * heading_id : 2
         * subheading : Basic Logic and Set Notation
         * video_id : 1
         * video_file : https://ebookpdfdump.s3.ap-south-1.amazonaws.com/sample-mp4-file.mp4
         * video_name : Test Video.mp4
         * video_size : 10 MB
         * video_cover : SampleVideoCover.png
         * video_duration : 10 min
         */

        @SerializedName("chapter_name")
        public String chapterName;
        @SerializedName("chapter_id")
        public String chapterId;
        @SerializedName("heading_name")
        public String headingName;
        @SerializedName("heading_id")
        public String headingId;
        @SerializedName("subheading")
        public String subheading;
        @SerializedName("video_id")
        public String videoId;
        @SerializedName("video_file")
        public String videoFile;
        @SerializedName("video_name")
        public String videoName;
        @SerializedName("video_size")
        public String videoSize;
        @SerializedName("video_cover")
        public String videoCover;
        @SerializedName("video_duration")
        public String videoDuration;
    }
}