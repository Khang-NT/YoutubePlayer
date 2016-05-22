package com.kapp.youtube.background.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.devspark.robototextview.widget.RobotoTextView;
import com.kapp.youtube.background.R;
import com.kapp.youtube.background.model.IMedia;

import java.util.List;

/**
 * Created by khang on 22/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class PlayListViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "PlayListViewHolder";
    public final RecyclerView horizontalRecyclerView;
    private final RobotoTextView tvTitle;
    private MediaAdapter adapter;

    public PlayListViewHolder(View itemView, MediaAdapter adapter) {
        super(itemView);
        horizontalRecyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_recycler_view);
        tvTitle = (RobotoTextView) itemView.findViewById(R.id.tv_play_list_title);
        this.adapter = adapter;
    }

    public PlayListViewHolder initRecyclerView(Context context, View.OnScrollChangeListener scrollChangeListener) {
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        horizontalRecyclerView.setOnScrollChangeListener(scrollChangeListener);
        horizontalRecyclerView.setAdapter(adapter);
        horizontalRecyclerView.setHasFixedSize(true);
        horizontalRecyclerView.setNestedScrollingEnabled(false);
        return this;
    }

    public void bindData(String title, List<IMedia> medias, int scrollX, int position) {
        tvTitle.setText(title);
        adapter.setMedias(medias);
        horizontalRecyclerView.scrollTo(scrollX, 0);
        horizontalRecyclerView.setTag(position);
    }
}
