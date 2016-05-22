package com.kapp.youtube.background.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kapp.youtube.background.R;
import com.kapp.youtube.background.model.IMedia;
import com.kapp.youtube.background.model.PlayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khang on 22/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class PlayListMediaAdapter<K extends IMedia> extends RecyclerView.Adapter<PlayListViewHolder> implements View.OnScrollChangeListener {
    private static final String TAG = "PlayListMediaAdapter";

    Context context;
    List<PlayList<K>> playLists;
    int[] scrollY;
    LayoutInflater inflater;

    public PlayListMediaAdapter(Context context, @NonNull List<PlayList<K>> playLists) {
        this.context = context;
        this.playLists = playLists;
        onDatasetChange();
        inflater = LayoutInflater.from(context);
    }

    public void setPlayLists(@NonNull List<PlayList<K>> playLists) {
        this.playLists = playLists;
        onDatasetChange();
        notifyDataSetChanged();
    }

    public void addPlaylist(PlayList<K> playList) {
        this.playLists.add(playList);
        onDatasetChange();
        notifyDataSetChanged();
    }

    private void onDatasetChange() {
        scrollY = new int[playLists.size()];
        for (int i = 0; i < playLists.size(); i++) {
            scrollY[i] = 0;
        }
    }

    @Override
    public PlayListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayListViewHolder(
                inflater.inflate(R.layout.horizontal_list, parent, false),
                new MediaAdapter<>(context, new ArrayList<K>())
        ).initRecyclerView(context, this);
    }

    @Override
    public void onBindViewHolder(PlayListViewHolder holder, int position) {
        PlayList playList = playLists.get(position);
        holder.bindData(playList.getTitle(), playList.getMedias(), scrollY[position], position);
    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int position = (int) v.getTag();
        if (position < playLists.size())
            this.scrollY[position] = scrollY;
    }
}
