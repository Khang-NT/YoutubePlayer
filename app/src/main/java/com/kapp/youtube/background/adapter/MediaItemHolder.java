package com.kapp.youtube.background.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kapp.youtube.background.R;
import com.kapp.youtube.background.model.IMedia;
import com.kapp.youtube.background.widget.MediaItem;

/**
 * Created by khang on 22/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class MediaItemHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MediaItemHolder";
    private final MediaItem mediaItem;

    public MediaItemHolder(View itemView) {
        super(itemView);
        mediaItem = (MediaItem) itemView.findViewById(R.id.media_item);
    }

    public void bindData(IMedia data) {
        mediaItem.bindData(data);
    }
}
