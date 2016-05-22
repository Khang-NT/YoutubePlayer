package com.kapp.youtube.background;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kapp.youtube.background.util.Settings;
import com.kapp.youtube.background.youtube.Auth;

/**
 * Created by khang on 21/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class MainApplication extends Application {
    private static final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.initialize(this);
        Auth.initialize(this);
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
    }
}
