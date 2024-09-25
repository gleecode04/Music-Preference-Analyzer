package com.example.spotifywrapped.data_classes;
import androidx.annotation.NonNull;

import java.util.Date;

public class Like {
    private int likeId;
    private String userId;
    private Date date;

    public Like(int likeId, String userId, Date date) {
        this.likeId = likeId;
        this.userId = userId;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }
}
