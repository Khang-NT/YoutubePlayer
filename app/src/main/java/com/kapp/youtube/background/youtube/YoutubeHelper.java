package com.kapp.youtube.background.youtube;

import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Activity;
import com.kapp.youtube.background.SecretConstants;

import java.io.IOException;
import java.util.List;

/**
 * Created by khang on 19/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class YoutubeHelper {

    private static final String APP_NAME = "Youtube player";

    protected static YouTube youTube;

    public static void getActivities() throws IOException {
        Credential credential = Auth.authorize();
        youTube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(APP_NAME)
                .build();
        try {
            List<Activity> list = youTube.activities().list("snippet")
                    .setKey(SecretConstants.API_KEY)
                    .setHome(true)
                    .setMaxResults(50L)
                    .execute().getItems();
            if (list != null)
                for (Activity activity : list) {
                    Log.e("TAG", "YoutubeHelper - line 43: " + activity.getSnippet().getChannelTitle());
                }
        } catch (IOException e) {
            Log.e("TAG", "getActivities - line 44: " + e);
        }


    }
}
