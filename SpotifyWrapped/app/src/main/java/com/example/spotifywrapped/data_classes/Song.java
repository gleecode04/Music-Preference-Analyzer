package com.example.spotifywrapped.data_classes;

import androidx.annotation.NonNull;

public class Song {
    private final String name;
    private final String artist;
    private final String album;
    private final String preview;
    private final long popularity;

    public Song() {
        this("", "", "", "", 0);
    }
    public Song(String name, String artist, String album, String preview, long popularity) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.preview = preview;
        this.popularity = popularity;
    }
    public Song(String name) {
        this(name, "","","",0);
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getPreview() {
        return preview;
    }

    public long getPopularity() {
        return popularity;
    }

    @NonNull
    @Override
    public String toString() {
        return "Song{" +
                name + ", " +
                artist + ", " +
                album + ", " +
                preview + ", " +
                popularity + "}";
    }
}
