package com.kapp.youtube.background.model;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by khang on 19/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public interface IMedia {
    String getTitle();

    String getDescription();

    String getDuration();

    void bindThumbnail(ImageView imageView, Picasso picasso);
}
