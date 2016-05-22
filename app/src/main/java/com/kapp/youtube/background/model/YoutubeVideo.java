package com.kapp.youtube.background.model;

import android.net.Uri;
import android.widget.ImageView;

import com.kapp.youtube.background.R;
import com.squareup.picasso.Picasso;

/**
 * Created by khang on 22/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class YoutubeVideo implements IMedia {
    private static final String TAG = "YoutubeVideo";

    private String id;
    private String title;
    private String channelTitle;
    private String duration;
    private String thumbnailUrl;

    public YoutubeVideo(String id, String title, String channelTitle, String duration, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.channelTitle = channelTitle;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return channelTitle;
    }

    @Override
    public String getDuration() {
        return duration.replace("PT", "")
                .replace("H", ":")
                .replace("M", ":")
                .replace("S", ":");
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

//    @Override
//    public int getCardColor() {
//        return Color.WHITE;
//    }

    @Override
    public void bindThumbnail(ImageView imageView, Picasso picasso) {
        picasso.load(Uri.parse(thumbnailUrl))
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
}
