package com.example.spotifywrapped.data_classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    //private String commentId;
    private String userId;
    private String text;
    private String date;

    public Comment(String userId, String text, String date) {
        this.userId = userId;
        this.text = text;
        this.date = date;
    }
    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    @Override

    public String toString() {
        return String.format(userId + "/" + text + "/" + date);
    }

}
