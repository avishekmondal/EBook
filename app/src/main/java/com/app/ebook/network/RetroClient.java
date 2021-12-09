package com.app.ebook.network;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.app.ebook.R;
import com.app.ebook.models.BoardListResponse;
import com.app.ebook.models.CheckDuplicateProfileRequest;
import com.app.ebook.models.CheckDuplicateProfileResponse;
import com.app.ebook.models.CheckDuplicateUserRequest;
import com.app.ebook.models.CheckDuplicateUserResponse;
import com.app.ebook.models.CityListRequest;
import com.app.ebook.models.CityListResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.models.ForgotPasswordRequest;
import com.app.ebook.models.ForgotPasswordResponse;
import com.app.ebook.models.InstituteListResponse;
import com.app.ebook.models.LoginRequest;
import com.app.ebook.models.LogoutResponse;
import com.app.ebook.models.RegistrationRequest;
import com.app.ebook.models.RegistrationResponse;
import com.app.ebook.models.ResendOTPRequest;
import com.app.ebook.models.ResendOTPResponse;
import com.app.ebook.models.ResetPasswordRequest;
import com.app.ebook.models.ResetPasswordResponse;
import com.app.ebook.models.SendOTPRequest;
import com.app.ebook.models.SendOTPResponse;
import com.app.ebook.models.StateListResponse;
import com.app.ebook.models.UpdateUserDetailsRequest;
import com.app.ebook.models.UpdateUserDetailsResponse;
import com.app.ebook.models.UserDetailsResponse;
import com.app.ebook.models.book_chapters.AudioListRequest;
import com.app.ebook.models.book_chapters.AudioListResponse;
import com.app.ebook.models.book_chapters.BookChapterListRequest;
import com.app.ebook.models.book_chapters.BookChapterListResponse;
import com.app.ebook.models.book_chapters.VideoListRequest;
import com.app.ebook.models.book_chapters.VideoListResponse;
import com.app.ebook.models.book_details.BookContentRequest;
import com.app.ebook.models.book_details.BookContentResponse;
import com.app.ebook.models.book_list.BookListRequest;
import com.app.ebook.models.book_list.BookListResponse;
import com.app.ebook.models.book_subscription.AddToSubscriptionRequest;
import com.app.ebook.models.book_subscription.AddToSubscriptionResponse;
import com.app.ebook.models.book_subscription.SubscriptionListRequest;
import com.app.ebook.models.book_subscription.SubscriptionListResponse;
import com.app.ebook.models.book_subscription.SubscriptionPlanListRequest;
import com.app.ebook.models.book_subscription.SubscriptionPlanListResponse;
import com.app.ebook.models.cart.CartListRequest;
import com.app.ebook.models.cart.CartListResponse;
import com.app.ebook.models.cart.UpdateCartRequest;
import com.app.ebook.models.cart.UpdateCartResponse;
import com.app.ebook.models.exam_prep_chapters.ExamPrepChapterListRequest;
import com.app.ebook.models.exam_prep_chapters.ExamPrepChapterListResponse;
import com.app.ebook.models.exam_prep_chapters.MCQCategoryListRequest;
import com.app.ebook.models.exam_prep_chapters.MCQCategoryListResponse;
import com.app.ebook.models.mcq.MCQListRequest;
import com.app.ebook.models.mcq.MCQListResponse;
import com.app.ebook.models.subjective.SubjectiveListRequest;
import com.app.ebook.models.subjective.SubjectiveListResponse;
import com.app.ebook.models.wish_list.AddToWishListRequest;
import com.app.ebook.models.wish_list.AddToWishListResponse;
import com.app.ebook.ui.activity.BaseActivity;
import com.app.ebook.ui.activity.LoginActivity;
import com.app.ebook.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class RetroClient {

    Context mContext;
    public Retrofit retrofit;
    RetrofitListener retroListener;
    String method_name;
    String error_message;

    public RetroClient(Context mContext, RetrofitListener retroListener) {
        this.mContext = mContext;
        this.retroListener = retroListener;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Search logcat with OkHttp: for raw response : addInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization",
                                "Bearer " + ((BaseActivity) mContext).mSessionManager.getSession(Constants.USER_TOKEN)).build();
                        return chain.proceed(request);
                    }
                })
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * Network Request
     *
     * @param call
     * @param method
     */
    public void makeHttpRequest(Call call, final String method) {
        this.method_name = method;

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Log.v("TAG", "onResponse :" + method_name);
                    retroListener.onSuccess(call, response, method_name);
                } else {
                    if (response.code() == 401) {
                        error_message = "Session Timed Out.";
                        Toast.makeText(mContext, error_message, Toast.LENGTH_LONG).show();
                        ((BaseActivity) mContext).mSessionManager.setSession(Constants.IS_LOGGEDIN, false);
                        ((BaseActivity) mContext).mSessionManager.setSession(Constants.USER_TOKEN, "");
                        ((BaseActivity) mContext).startTargetActivityNewTask(LoginActivity.class);
                        ((BaseActivity) mContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else {
                        switch (method) {
                            case UrlConstants.URL_LOGIN:
                                error_message = "Unable to log in with provided credentials.";
                                break;
                            default:
                                error_message = "";
                                break;
                        }
                    }
                    retroListener.onFailure(error_message);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (t instanceof NoRouteToHostException) {
                    Log.e("TAG", "onFailure1 : server unreach");
                    retroListener.onFailure("Server Unreachable");
                } else if (t instanceof SocketTimeoutException) {
                    Log.e("TAG", "onFailure2 : time out");
                    retroListener.onFailure("Server Unreachable");
                } else if (t instanceof IOException) {
                    Log.e("TAG", "onFailure3 : No internet");
                    retroListener.onFailure("No Internet");
                } else {
                    Log.e("TAG", "onFailure4 : " + t.getMessage());
                    retroListener.onFailure(t.getMessage());
                }
            }
        });
    }

    public interface RestInterface {

        @GET(UrlConstants.URL_BOARD_LIST)
        Call<List<BoardListResponse>> getBoardList();

        @GET(UrlConstants.URL_CLASS_LIST)
        Call<List<ClassListResponse>> getClassList();

        @GET(UrlConstants.URL_STATE_LIST)
        Call<List<StateListResponse>> getStateList();

        @POST(UrlConstants.URL_CITY_LIST)
        Call<CityListResponse> getCityList(@Body CityListRequest body);

        @POST(UrlConstants.URL_CHECK_DUPLICATE_USER)
        Call<CheckDuplicateUserResponse> checkDuplicateUser(@Body CheckDuplicateUserRequest body);

        @POST(UrlConstants.URL_REGISTRATION)
        Call<RegistrationResponse> registration(@Body RegistrationRequest body);

        @POST(UrlConstants.URL_LOGIN)
        Call<UserDetailsResponse> login(@Body LoginRequest body);

        @POST(UrlConstants.URL_SEND_OTP)
        Call<SendOTPResponse> sendOTP(@Body SendOTPRequest body);

        @POST(UrlConstants.URL_RESEND_OTP)
        Call<ResendOTPResponse> resendOTP(@Body ResendOTPRequest body);

        @POST(UrlConstants.URL_FORGOT_PASSWORD)
        Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest body);

        @POST(UrlConstants.URL_RESET_PASSWORD)
        Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest body);

        @POST(UrlConstants.URL_CHECK_DUPLICATE_PROFILE)
        Call<CheckDuplicateProfileResponse> checkDuplicateProfile(@Body CheckDuplicateProfileRequest body);

        @POST(UrlConstants.URL_UPDATE_USER_DETAILS)
        Call<UpdateUserDetailsResponse> updateUserDetails(@Body UpdateUserDetailsRequest body);

        @POST(UrlConstants.URL_BOOK_LIST)
        Call<BookListResponse> getBookList(@Body BookListRequest body);

        @POST(UrlConstants.URL_BOOK_CONTENT)
        Call<BookContentResponse> getBookContent(@Body BookContentRequest body);

        // Preview Details
        @POST(UrlConstants.URL_PREVIEW_BOOK_CHAPTER_LIST)
        Call<BookChapterListResponse> getPreviewBookChapterList(@Body BookChapterListRequest body);

        @POST(UrlConstants.URL_PREVIEW_AUDIO_LIST)
        Call<AudioListResponse> getPreviewAudioList(@Body AudioListRequest body);

        @POST(UrlConstants.URL_PREVIEW_VIDEO_LIST)
        Call<VideoListResponse> getPreviewVideoList(@Body VideoListRequest body);

        @POST(UrlConstants.URL_MCQ_CATEGORY_LIST)
        Call<MCQCategoryListResponse> getMCQCategoryList(@Body MCQCategoryListRequest body);

        @POST(UrlConstants.URL_PREVIEW_MCQ_CHAPTER_LIST)
        Call<ExamPrepChapterListResponse> getPreviewMcqChapterList(@Body ExamPrepChapterListRequest body);

        @POST(UrlConstants.URL_PREVIEW_MCQ_LIST)
        Call<MCQListResponse> getPreviewMCQList(@Body MCQListRequest body);

        @POST(UrlConstants.URL_PREVIEW_SUBJECTIVE_CHAPTER_LIST)
        Call<ExamPrepChapterListResponse> getPreviewSubjectiveChapterList(@Body ExamPrepChapterListRequest body);

        @POST(UrlConstants.URL_PREVIEW_SUBJECTIVE_LIST)
        Call<SubjectiveListResponse> getPreviewSubjectiveList(@Body SubjectiveListRequest body);

        // Library Details
        @POST(UrlConstants.URL_BOOK_CHAPTER_LIST)
        Call<BookChapterListResponse> getBookChapterList(@Body BookChapterListRequest body);

        @POST(UrlConstants.URL_AUDIO_LIST)
        Call<AudioListResponse> getAudioList(@Body AudioListRequest body);

        @POST(UrlConstants.URL_VIDEO_LIST)
        Call<VideoListResponse> getVideoList(@Body VideoListRequest body);

        @POST(UrlConstants.URL_MCQ_CHAPTER_LIST)
        Call<ExamPrepChapterListResponse> getMcqChapterList(@Body ExamPrepChapterListRequest body);

        @POST(UrlConstants.URL_MCQ_LIST)
        Call<MCQListResponse> getMCQList(@Body MCQListRequest body);

        @POST(UrlConstants.URL_SUBJECTIVE_CHAPTER_LIST)
        Call<ExamPrepChapterListResponse> getSubjectiveChapterList(@Body ExamPrepChapterListRequest body);

        @POST(UrlConstants.URL_SUBJECTIVE_LIST)
        Call<SubjectiveListResponse> getSubjectiveList(@Body SubjectiveListRequest body);

        ////////////////////////////////////////////////////////////////////////////////////////////

        @POST(UrlConstants.URL_BOOK_AUDIO_LIST)
        Call<BookChapterListResponse> getBookAudioList(@Body BookChapterListRequest body);

        @POST(UrlConstants.URL_BOOK_VIDEO_LIST)
        Call<BookChapterListResponse> getBookVideoList(@Body BookChapterListRequest body);

        @POST(UrlConstants.URL_WISH_LIST)
        Call<BookListResponse> getWishList();

        @POST(UrlConstants.URL_ADD_WISH_LIST)
        Call<AddToWishListResponse> addWishList(@Body AddToWishListRequest body);

        @POST(UrlConstants.URL_DELETE_WISH_LIST)
        Call<AddToWishListResponse> deleteWishList(@Body AddToWishListRequest body);

        @POST(UrlConstants.URL_LIBRARY_LIST)
        Call<BookListResponse> getLibraryList();

        @POST(UrlConstants.URL_SUBSCRIPTION_PLAN_LIST)
        Call<SubscriptionPlanListResponse> getSubscriptionPlanList(@Body SubscriptionPlanListRequest body);

        @POST(UrlConstants.URL_ADD_CART)
        Call<UpdateCartResponse> addToCart(@Body UpdateCartRequest body);

        @POST(UrlConstants.URL_DELETE_CART)
        Call<UpdateCartResponse> deleteFromCart(@Body UpdateCartRequest body);

        @POST(UrlConstants.URL_CART_LIST)
        Call<CartListResponse> getCartList(@Body CartListRequest body);

        @POST(UrlConstants.URL_ADD_SUBSCRIPTION_LIST)
        Call<AddToSubscriptionResponse> addSubscriptionList(@Body AddToSubscriptionRequest body);

        @POST(UrlConstants.URL_SUBSCRIPTION_LIST)
        Call<SubscriptionListResponse> getSubscriptionList(@Body SubscriptionListRequest body);

        @GET(UrlConstants.URL_LOGOUT)
        Call<LogoutResponse> logout();

        @GET(UrlConstants.URL_INSTITUTE_LIST)
        Call<InstituteListResponse> instituteList();

        @GET(UrlConstants.URL_CITY_LIST)
        Call<CityListResponse> cityList(@Query("stateId") String stateId);

        @GET(UrlConstants.URL_STATE_LIST)
        Call<StateListResponse> stateList();

    }

}
