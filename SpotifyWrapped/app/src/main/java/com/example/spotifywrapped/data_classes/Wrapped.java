package com.example.spotifywrapped.data_classes;

import java.util.ArrayList;
import java.util.List;

public class Wrapped {
    private ArrayList<Artist>topArtists;
    private ArrayList<Song> topSongs;
    private int minutesListened;
    private String topGenre;

    public Wrapped(ArrayList<Artist> topArtists, ArrayList<Song> topSongs, int minutesListened, String topGenre) {
        this.topArtists = topArtists;
        this.topSongs = topSongs;
        this.minutesListened = minutesListened;
        this.topGenre = topGenre;
    }

    public ArrayList<Artist> getTopArtists() {
        return topArtists;
    }

    public ArrayList<Song> getTopSongs() {
        return topSongs;
    }

    public int getMinutesListened() {
        return minutesListened;
    }

    public String getTopGenre() {
        return topGenre;
    }
}

