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
    public static final String REFRESH_TOKEN = "refreshToken";
    private static final String TAG = "Settings";
    private static Context context;
    private static Settings instance;

    private final SharedPreferences preferences;

    public Settings(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public synchronized static Settings getInstance() {
        if (instance == null)
            return (instance = new Settings(context.getSharedPreferences(TAG, Context.MODE_PRIVATE)));
        else
            return instance;
    }

    public static void initialize(Context context) {
        Settings.context = context;
        getInstance();
    }

    public void setUserInfo(String id, String authCode, String name, String email) {
        synchronized (preferences) {
            preferences.edit()
                    .putString(USER_ID, id)
                    .putString(AUTH_CODE, authCode)
                    .putString(USER_NAME, name)
                    .putString(USER_EMAIL, email)
                    .apply();
        }
    }

    public String getUserId() {
        synchronized (preferences) {
            return preferences.getString(USER_ID, null);
        }
    }

    public String getAuthCode() {
        synchronized (preferences) {
            return preferences.getString(AUTH_CODE, null);
        }
    }

    public String getUserName() {
        synchronized (preferences) {
            return preferences.getString(USER_NAME, null);
        }
    }

    public String getUserEmail() {
        synchronized (preferences) {
            return preferences.getString(USER_EMAIL, null);
        }
    }

    public String getRefreshToken() {
        synchronized (preferences) {
            return preferences.getString(REFRESH_TOKEN, null);
        }
    }

    public void setRefreshToken(String token) {
        synchronized (preferences) {
            preferences.edit()
                    .putString(REFRESH_TOKEN, token)
                    .apply();
        }
    }
}
