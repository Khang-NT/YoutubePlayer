package com.kapp.youtube.background.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kapp.youtube.background.R;
import com.kapp.youtube.background.adapter.PlayListMediaAdapter;
import com.kapp.youtube.background.model.PlayList;
import com.kapp.youtube.background.model.YoutubeVideo;
import com.kapp.youtube.background.youtube.YoutubeHelper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by khang on 19/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    PlayListMediaAdapter<YoutubeVideo> adapter;
    private View progressWheel;
    private RecyclerView verticalRecyclerView;

    public HomeFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        progressWheel = v.findViewById(R.id.progress_wheel);
        verticalRecyclerView = (RecyclerView) v.findViewById(R.id.vertical_recycler_view);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        verticalRecyclerView.setHasFixedSize(true);
        adapter = new PlayListMediaAdapter<>(getContext(), new ArrayList<PlayList<YoutubeVideo>>());
        verticalRecyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final PlayList<YoutubeVideo> playList = YoutubeHelper.getInstance().getRecommendedVideos();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addPlaylist(playList);
                            progressWheel.setVisibility(View.GONE);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
