package com.kapp.youtube.background.util;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by khang on 20/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class FirebaseNode {
    public static final String USERS = "users";
    public static final String SETTINGS = "settings";
    private static final String TAG = "FirebaseNode";

    public static DatabaseReference getUserNode(@NonNull String userId) {
        return FirebaseDatabase.getInstance()
                .getReference(USERS)
                .child(userId);
    }
}
