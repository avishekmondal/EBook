package com.app.ebook.network;

import retrofit2.Call;
import retrofit2.Response;


public interface RetrofitListener {
    void onSuccess(Call call, Response response, String method_name);
    void onFailure(String errorMessage);
    //void onError(String errorMessage);
}
