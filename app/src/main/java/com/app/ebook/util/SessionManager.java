package com.app.ebook.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "EBook";

    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String IS_VERIFIED = "is_verified";
    private static final String TOKEN = "token";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Save session in String value
     *
     * @param value
     */
    public void setSession(String Key, String value) {
        editor.putString(Key, value);
        editor.commit();
    }

    public String getSession(String Key) {
        return pref.getString(Key, "");
    }

    /**
     * Save session in String value
     *
     * @param value
     */
    public void setSession(String Key, Boolean value) {
        editor.putBoolean(Key, value);
        editor.commit();
    }

    public Boolean getBooleanSession(String Key) {
        return pref.getBoolean(Key, false);
    }

}