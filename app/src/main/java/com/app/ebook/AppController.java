package com.app.ebook;


import android.app.Application;
import android.content.Context;

public class AppController extends Application {
    public static volatile Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}