package com.kapp.youtube.background.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by khang on 21/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class Settings {
    public static final String AUTH_CODE = "authCode";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    private static final String TAG = "Settings";
    private static Context context;

    public static void initialize(Context context) {
        Settings.context = context;
    }

    public static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public static void setUserInfo(String id, String authCode, String name, String email) {
        getSharedPreferences().edit()
                .putString(USER_ID, id)
                .putString(AUTH_CODE, authCode)
                .putString(USER_NAME, name)
                .putString(USER_EMAIL, email)
                .apply();
    }

    public static String getUserId() {
        return getSharedPreferences().getString(USER_ID, null);
    }

    public static String getAuthCode() {
        return getSharedPreferences().getString(AUTH_CODE, null);
    }

    public static String getUserName() {
        return getSharedPreferences().getString(USER_NAME, null);
    }

    public static String getUserEmail() {
        return getSharedPreferences().getString(USER_EMAIL, null);
    }
}
