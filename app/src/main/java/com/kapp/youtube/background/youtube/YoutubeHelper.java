package com.kapp.youtube.background.youtube;

import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.Video;
import com.kapp.youtube.background.SecretConstants;
import com.kapp.youtube.background.model.PlayList;
import com.kapp.youtube.background.model.YoutubeVideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by khang on 19/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class YoutubeHelper {
    public static final String TAG = "YoutubeHelper";
    public static final String VIDEO_FIELDS = "items(snippet/thumbnails/high/url,snippet/channelTitle,snippet/title,contentDetails/duration,id,statistics/viewCount)";
    public static final String RECOMMENDED_TITLE = "Recommended for you";
    public static final String VIDEO_PART = "snippet,statistics,contentDetails,id";
    public static final String MOST_POPULAR_TITLE = "Most popular videos";
    private static final String APP_NAME = "Youtube player";
    private static final long MAX_RESULTS = 35L;
    private static YoutubeHelper instance;
    private final YouTube youTubeInstance;

    public YoutubeHelper(YouTube youTubeInstance) {
        this.youTubeInstance = youTubeInstance;
    }

    public static synchronized YoutubeHelper getInstance() throws IOException {
        if (instance != null)
            return instance;
        else {
            Credential credential = Auth.authorize();
            YouTube youTube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                    .setApplicationName(APP_NAME)
                    .build();
            return instance = new YoutubeHelper(youTube);
        }
    }

    private static List<YoutubeVideo> convert(List<Video> origin) {
        if (origin == null)
            return null;
        List<YoutubeVideo> result = new ArrayList<>();
        for (Video video : origin) {
            result.add(new YoutubeVideo(
                    video.getId(),
                    video.getSnippet().getTitle(),
                    video.getSnippet().getChannelTitle(),
                    video.getContentDetails().getDuration(),
                    video.getSnippet().getThumbnails().getHigh().getUrl()
            ));
        }
        return result;
    }

    public PlayList<YoutubeVideo> getRecommendedVideos() throws IOException {
        List<YoutubeVideo> videos = null;
        List<Activity> list;
        synchronized (youTubeInstance) {
            list = youTubeInstance.activities().list("contentDetails")
                    .setKey(SecretConstants.API_KEY)
                    .setHome(true)
                    .setMaxResults(MAX_RESULTS)
                    .setFields("items/contentDetails")
                    .execute().getItems();
        }
        if (list != null) {
            String ids = "";
            for (Activity activity : list) {
                if (activity.getContentDetails() != null) {
                    ActivityContentDetails details = activity.getContentDetails();
                    if (details.getRecommendation() != null)
                        ids = details.getRecommendation().getResourceId().getVideoId() + "," + ids;
                    else if (details.getUpload() != null)
                        ids = ids + details.getUpload().getVideoId() + ",";
                }
            }
            Log.e(TAG, "getRecommendedVideos - line 52: " + ids);
            if (ids.length() > 0) {
                videos = getVideoDetails(ids);
            }
        }
        return new PlayList<>(videos, "", RECOMMENDED_TITLE);
    }

    public PlayList<YoutubeVideo> getMostPopularVideos() throws IOException {
        List<Video> videos;
        synchronized (youTubeInstance) {
            videos = youTubeInstance.videos().list(VIDEO_PART)
                    .setFields(VIDEO_FIELDS)
                    .setChart("mostPopular")
                    .setRegionCode(Locale.getDefault().getCountry())
                    .setVideoCategoryId("10")
                    .setMaxResults(MAX_RESULTS).execute().getItems();
        }
        return new PlayList<>(convert(videos), "", MOST_POPULAR_TITLE);
    }

    public List<YoutubeVideo> getVideoDetails(String videoIds) throws IOException {
        List<Video> videos;
        synchronized (youTubeInstance) {
            videos = youTubeInstance.videos().list(VIDEO_PART)
                    .setFields(VIDEO_FIELDS)
                    .setId(videoIds)
                    .execute().getItems();
        }
        return convert(videos);
    }
}
