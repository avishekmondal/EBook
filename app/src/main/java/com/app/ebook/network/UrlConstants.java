package com.app.ebook.network;

public interface UrlConstants {

    String BASE_URL = "http://13.235.241.44:5000/";

    // Board & Class List
    String URL_BOARD_LIST = BASE_URL + "user/get_boards/";
    String URL_CLASS_LIST = BASE_URL + "user/get_class/";
    String URL_STATE_LIST = BASE_URL + "user/get_states/";
    String URL_CITY_LIST = BASE_URL + "user/get_city_by_state/";

    // Registration, Login, Forgot Password
    String URL_CHECK_DUPLICATE_USER = BASE_URL + "user/verify_registration/";
    String URL_REGISTRATION = BASE_URL + "user/registration/";
    String URL_LOGIN = BASE_URL + "user/login/";
    String URL_SEND_OTP = BASE_URL + "user/sendOTP/";
    String URL_RESEND_OTP = BASE_URL + "user/resend_OTP/";
    String URL_FORGOT_PASSWORD = BASE_URL + "user/ForgotPassword/";
    String URL_RESET_PASSWORD = BASE_URL + "user/resetPassword/";

    // Profile
    String URL_CHECK_DUPLICATE_PROFILE = BASE_URL + "user/verify_edit_profile/";
    String URL_UPDATE_USER_DETAILS = BASE_URL + "user/update_profile/";

    // Book List & Details
    String URL_BOOK_LIST = BASE_URL + "book/getallbooks/";
    String URL_BOOK_CONTENT = BASE_URL + "book/get_bookcontent/";

    String URL_BOOK_AUDIO_LIST = BASE_URL + "book/get_all_audio_by_book/";
    String URL_BOOK_VIDEO_LIST = BASE_URL + "book/get_all_videos_by_book/";

    // Preview Details
    String URL_PREVIEW_BOOK_CHAPTER_LIST = BASE_URL + "book/preview_get_chapters_by_books/";
    String URL_PREVIEW_AUDIO_LIST = BASE_URL + "book/preview_get_all_audios/";
    String URL_PREVIEW_VIDEO_LIST = BASE_URL + "book/preview_get_all_videos/";

    String URL_MCQ_CATEGORY_LIST = BASE_URL + "book/get_book_services/";
    String URL_PREVIEW_MCQ_CHAPTER_LIST = BASE_URL + "book/preview_get_mcqchapterlist_by_book/";
    String URL_PREVIEW_MCQ_LIST = BASE_URL + "book/preview_get_mqcquestions_by_chapter/";

    String URL_PREVIEW_SUBJECTIVE_CHAPTER_LIST = BASE_URL + "book/preview_get_chapterlist_by_book_desc/";
    String URL_PREVIEW_SUBJECTIVE_LIST = BASE_URL + "book/preview_get_desc_quest_by_chapter/";

    // Library Details
    String URL_BOOK_CHAPTER_LIST = BASE_URL + "book/get_chapters_by_books/";
    String URL_AUDIO_LIST = BASE_URL + "book/get_all_audios/";
    String URL_VIDEO_LIST = BASE_URL + "book/get_all_videos/";

    String URL_MCQ_CHAPTER_LIST = BASE_URL + "book/get_mcqchapterlist_by_book/";
    String URL_MCQ_LIST = BASE_URL + "book/get_mqcquestions_by_chapter/";

    String URL_SUBJECTIVE_CHAPTER_LIST = BASE_URL + "book/get_chapterlist_by_book_desc/";
    String URL_SUBJECTIVE_LIST = BASE_URL + "book/get_desc_quest_by_chapter/";

    // Wish Book List
    String URL_WISH_LIST = BASE_URL + "user/getmywishlist/";
    String URL_ADD_WISH_LIST = BASE_URL + "user/addtowishlist/";
    String URL_DELETE_WISH_LIST = BASE_URL + "user/deletewishlist/";

    // Library Book List
    String URL_LIBRARY_LIST = BASE_URL + "book/get_my_library/";

    // Book Subscription
    String URL_SUBSCRIPTION_PLAN_LIST = BASE_URL + "book/getproduct_by_book/";
    String URL_ADD_SUBSCRIPTION_LIST = BASE_URL + "book/addtomySubcription/";
    String URL_SUBSCRIPTION_LIST = BASE_URL + "book/get_my_subscribed_product_history/";

    // Cart
    String URL_ADD_CART = BASE_URL + "book/addtocart/";
    String URL_DELETE_CART = BASE_URL + "book/deletecart/";
    String URL_CART_LIST = BASE_URL + "book/getmycart/";

    String URL_CHANGE_PASSWORD = BASE_URL + "changePassword";
    String URL_LOGOUT = BASE_URL + "logout";
    String URL_INSTITUTE_LIST = BASE_URL + "institute/instituteList";

}
