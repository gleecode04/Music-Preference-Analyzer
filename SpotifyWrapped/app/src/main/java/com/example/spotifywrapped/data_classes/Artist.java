package com.example.spotifywrapped.data_classes;

import java.util.List;

public class Artist {
    private final String name;
    private final String picture;
    private final List<String> genres;
    private final long popularity;

    public Artist() {
        this("", "", null, 0);
    }

    public Artist(String name) {
        this(name, "", null, 0);
    }

    public Artist(String name, String picture, List<String> genres, int popularity) {
        this.name = name;
        this.picture = picture;
        this.genres = genres;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public List<String> getGenres() {
        return genres;
    }

    public long getPopularity() {
        return popularity;
    }
}
