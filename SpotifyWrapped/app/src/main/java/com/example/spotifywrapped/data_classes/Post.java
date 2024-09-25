package com.example.spotifywrapped.data_classes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    private User user;

    private final String postId;
    private String caption;
    private Date date;
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    private List<Like> likes;
    private int likeCount = 0;

    public Post(User user) {
        this.user = user;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.postId = String.format("p" + user.getPosts().size() + user.getName() );
        //comments.add(new Comment("1", "e", new Date()));
    }

    public void addLike(User user, Date date) {
        likeCount++;
        likes.add(new Like(likeCount, user.getSpotify_id(), date));
        user.addLikedPost(this);
    }

    public void removeLike(User user) {
        for (Like l : likes) {
            if (l.getUserId().equals(user.getSpotify_id())) {
                likes.remove(l);
                likeCount--;
                user.removeLikedPost(this);
            }
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Like> getLikes() {
        return likes;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public User getUser() {
        return user;
    }

    public String getPostId(){
        return postId;
    }
}
