package com.example.spotifywrapped.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spotifywrapped.data_classes.Like;
import com.example.spotifywrapped.data_classes.Post;

import com.example.spotifywrapped.data_classes.Comment;
import com.example.spotifywrapped.data_classes.User;

import java.util.ArrayList;
import java.util.List;

public class viewmodel extends ViewModel {

    // Define a MutableLiveData object to hold the list of custom objects
    private MutableLiveData<List<Post>> PostData = new MutableLiveData<>();
    private MutableLiveData<List<Post>> commentedPosts = new MutableLiveData<>();

    private MutableLiveData<List<Post>> likedPosts = new MutableLiveData<>();
    private static viewmodel instance;

    // Constructor to initialize the list
    private viewmodel() {
        // Initialize the list with an empty ArrayList
        PostData.setValue(new ArrayList<>());
        commentedPosts.setValue(new ArrayList<>());
        likedPosts.setValue(new ArrayList<>());
    }

    public void resetData() {
        PostData.setValue(new ArrayList<>());
    }

    public static viewmodel getInstance() {
        if (instance == null) {
            instance = new viewmodel();
        }
        return instance;
    }

    // Method to get the LiveData object
    public MutableLiveData<List<Post>> getCustomObjectListLiveData() {
        return PostData;
    }

    // Method to add a custom object to the list
    public void addPost(Post customObject) {
        Log.d("Test", customObject.getUser().getName());
        List<Post> currentList = PostData.getValue();
        currentList.add(customObject);
        PostData.setValue(currentList);
    }

    public void addAll(List<Post> posts) {
        for (Post p : posts) {
            addPost(p);
        }
    }

    // Method to remove a custom object from the list
    public void removePost(Post post) {
        List<Post> currentList = PostData.getValue();
        currentList.remove(post);
        PostData.setValue(currentList);
    }

    // Method to update a custom object in the list
    public void updatePost(Post updatedPost) {
        List<Post> currentList = PostData.getValue();
        for (int i = 0; i < currentList.size(); i++) {
            Post post = currentList.get(i);
            if (post.getPostId() == updatedPost.getPostId()) {
                currentList.set(i, updatedPost);
                break;
            }
        }
        PostData.setValue(currentList);
    }

    public MutableLiveData<List<Post>> getPostLiveData() {
        return PostData;
    }

    public MutableLiveData<List<Post>> getLikedPostsLiveData() {
        return likedPosts;
    }

    public MutableLiveData<List<Post>> getCommentedPostsLiveData() {
        return commentedPosts;
    }

    public void updateCommentedPosts(User u) {
        for (Post p : PostData.getValue()) {
            for (Comment c : p.getComments()) {
                if (c.getUserId() == u.getSpotify_id()) {
                    commentedPosts.getValue().add(p);
                    break;
                }
            }
        }
    }

    public void addCommentedPosts(Post p) {
        List<Post> currList = commentedPosts.getValue();
        currList.add(p);
        commentedPosts.setValue(currList);
    }

    public void addLikedPosts(Post p) {
        List<Post> currList = likedPosts.getValue();
        currList.add(p);
        likedPosts.setValue(currList);
    }

    public void updateLikedPosts(User u) {
        for (Post p : PostData.getValue()) {
            for (Like l : p.getLikes()) {
                if (l.getUserId() == u.getSpotify_id()) {
                    likedPosts.getValue().add(p);
                    break;
                }
            }
        }
    }

    public void removeLikedPosts(Post post) {
        List<Post> currList = likedPosts.getValue();
        currList.remove(post);
        likedPosts.setValue(currList);
    }

    public void printValues() {
        System.out.println("posts in vm");
        for (Post p : PostData.getValue()) {
            System.out.println(p.toString());
        }
        System.out.println("posts commented in vm");
        for (Post p : commentedPosts.getValue()) {
            System.out.println(p.toString());
        }
        System.out.println("posts liked in vm");
        for (Post p : likedPosts.getValue()) {
            System.out.println(p.toString());
        }
    }
}

