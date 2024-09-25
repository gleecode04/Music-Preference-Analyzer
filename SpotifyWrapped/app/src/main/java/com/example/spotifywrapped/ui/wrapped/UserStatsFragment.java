package com.example.spotifywrapped.ui.wrapped;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotifywrapped.R;
import com.example.spotifywrapped.UserStatAdapter;
import com.example.spotifywrapped.data_classes.Stat;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class UserStatsFragment extends Fragment {

    private RecyclerView rvTopArtists, rvTopSongs, rvMinutesListened, rvTopGenre;
    // Assuming you have separate adapter classes for each RecyclerView
    private UserStatAdapter adapterTopArtists, adapterTopSongs, adapterMinutesListened, adapterTopGenre;
    private List<Stat> topArtists, topSongs;
    private List<Stat> minutesListened, topGenre; // If these are lists

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_stats, container, false);

        // Initialize your data here - Example initialization
        topArtists = new ArrayList<>(); // Populate this list with your data
        topSongs = new ArrayList<>(); // Populate this list with your data
        minutesListened = new ArrayList<>(); // This might just have one Stat element
        topGenre = new ArrayList<>(); // This might just have one Stat element
        topArtists.add(new Stat ("post malone", "1"));
        topSongs.add(new Stat ("circles", "1"));
        minutesListened.add(new Stat ("365", "1"));
        topGenre.add(new Stat ("r&b / hiphop", "1"));
        Log.d("hm",topArtists.get(0).toString());
        Log.d("hm",topGenre.get(0).toString());
        // Setup RecyclerView for Top Artists
        rvTopArtists = view.findViewById(R.id.rvTopArtists);
        rvTopArtists.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterTopArtists = new UserStatAdapter(topArtists);
        rvTopArtists.setAdapter(adapterTopArtists);

        // Setup RecyclerView for Top Songs
        rvTopSongs = view.findViewById(R.id.rvTopSongs);
        rvTopSongs.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterTopSongs = new UserStatAdapter(topSongs);
        rvTopSongs.setAdapter(adapterTopSongs);

        // Setup RecyclerView for Minutes Listened - assuming minutesListened has only one item
        rvMinutesListened = view.findViewById(R.id.rvMinutesListened);
        rvMinutesListened.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterMinutesListened = new UserStatAdapter(minutesListened);
        rvMinutesListened.setAdapter(adapterMinutesListened);

        // Setup RecyclerView for Top Genre - assuming topGenre has only one item
        rvTopGenre = view.findViewById(R.id.rvTopGenre);
        rvTopGenre.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapterTopGenre = new UserStatAdapter(topGenre);
        rvTopGenre.setAdapter(adapterTopGenre);

        return view;
    }
}
