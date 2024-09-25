package com.example.spotifywrapped.data_classes;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class User {
    private String profile_picture;
    private ArrayList<Song> topFiveSongs;
    private ArrayList<Artist> topFiveArtists;

    private String spotify_id;
    private String name;
    private List<Post> posts = new ArrayList<>();
    private List<User> followers;
    public List<User> following = new ArrayList<>();

    public List<Post> likedPosts = new ArrayList<>();

    private boolean visibility;

    public User(String userId, String name) {
        this.spotify_id = userId;
        this.name = name;
        this.posts = new ArrayList<>();
        this.likedPosts = new ArrayList<>();
        this.topFiveSongs = new ArrayList<>();
        this.topFiveArtists = new ArrayList<>();

    }


    public User(DocumentSnapshot userDocument) {
        this.name = userDocument.getString("name");
        this.profile_picture = userDocument.getString("profile_picture");
        this.spotify_id = userDocument.getString("spotify_id");
        this.topFiveSongs = convertToSongList(userDocument.get("topFiveSongs"));
        this.topFiveArtists = convertToArtistList(userDocument.get("topFiveArtists"));
    }
    private ArrayList<Song> convertToSongList(Object rawData) {
        ArrayList<Song> songs = new ArrayList<>();
        if (rawData instanceof List) {
            List<Map<String, Object>> songData = (List<Map<String, Object>>) rawData;
            for (Map<String, Object> data : songData) {
                songs.add(new Song(
                        (String) data.get("name"),
                        (String) data.get("artist"),
                        (String) data.get("album"),
                        (String) data.get("preview"),
                        ((Number) data.get("popularity")).longValue()
                ));
            }
        }
        return songs;
    }

    private ArrayList<Artist> convertToArtistList(Object rawData) {
        ArrayList<Artist> artists = new ArrayList<>();
        if (rawData instanceof List) {
            List<Map<String, Object>> artistData = (List<Map<String, Object>>) rawData;
            for (Map<String, Object> data : artistData) {
                List<String> genres = (List<String>) data.get("genres");
                artists.add(new Artist(
                        (String) data.get("name"),
                        (String) data.get("picture"),
                        genres,
                        ((Number) data.get("popularity")).intValue()
                ));
            }
        }
        return artists;
    }

    public String getName() {
        return name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost() {
        this.posts.add(new Post(this));
    }

    public List<Post> getLikedPosts() {
        return this.likedPosts;
    }

    public void addLikedPost(Post post) {
        likedPosts.add(post);
    }
    public void removeLikedPost(Post post) {
        likedPosts.remove(post);
    }

    public ArrayList<Artist> getTopFiveArtists() {
        return topFiveArtists;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getSpotify_id() {
        return spotify_id;
    }

    public ArrayList<Song> getTopFiveSongs() {
        return topFiveSongs;
    }

    public void setTopFiveSongs(ArrayList<Song> s) {
        this.topFiveSongs = s;
    }
    public void setTopFiveArtists(ArrayList<Artist> a) {
        this.topFiveArtists = a;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", spotify_id='" + spotify_id + '\'' +
                ", topFiveSongs=" + topFiveSongs +
                ", topFiveArtists=" + topFiveArtists +
                '}';
    }
}
