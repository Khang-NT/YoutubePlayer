package com.kapp.youtube.background.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kapp.youtube.background.R;
import com.kapp.youtube.background.model.IMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khang on 22/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class MediaAdapter<T extends IMedia> extends RecyclerView.Adapter<MediaItemHolder> {
    private static final String TAG = "MediaAdapter";

    List<T> medias = new ArrayList<>();
    LayoutInflater inflater;

    public MediaAdapter(Context context, @NonNull List<T> medias) {
        this.medias = medias;
        inflater = LayoutInflater.from(context);
    }

    public MediaAdapter setMedias(List<T> medias) {
        if (medias == null)
            this.medias = new ArrayList<>();
        else
            this.medias = medias;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public MediaItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaItemHolder(inflater.inflate(R.layout.media_item_horizontal, parent, false));
    }

    @Override
    public void onBindViewHolder(MediaItemHolder holder, int position) {
        holder.bindData(medias.get(position));
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }
}
