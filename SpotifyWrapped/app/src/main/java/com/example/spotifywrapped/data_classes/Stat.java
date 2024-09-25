package com.example.spotifywrapped.data_classes;

public class Stat {

    private String title;
    private String detail;

    public Stat(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    // Setters, if needed
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    // Optionally override toString() if needed for debugging
    @Override
    public String toString() {
        return "Stat{" +
                "title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
