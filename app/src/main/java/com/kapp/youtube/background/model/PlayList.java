package com.kapp.youtube.background.model;

import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

/**
 * Created by khang on 22/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class PlayList<T extends IMedia> {
    private static final String TAG = "PlayList";

    List<T> medias;
    private String id;
    private String title;

    public PlayList(List<T> medias, String id, String title) {
        this.medias = medias;
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<T> getMedias() {
        return medias;
    }

    public void setMedias(List<T> medias) {
        this.medias = medias;
    }

    public int size() {
        return medias == null ? 0 : medias.size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return size() + " item" + (size() > 1 ? "s" : "");
    }

    public void bindThumbnail(ImageView imageView) {
        if (title == null || title.length() < 2)
            title = "Pl";
        String s = title.substring(0, 1).toUpperCase()
                + title.substring(1, 2).toLowerCase();
        TextDrawable textDrawable = TextDrawable.builder()
                .buildRect(s, ColorGenerator.MATERIAL.getColor(s));
        imageView.setImageDrawable(textDrawable);
    }


}
