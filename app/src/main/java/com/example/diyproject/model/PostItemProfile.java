package com.example.diyproject.model;

public class PostItemProfile {
    public int id;
    public int imageResId;

    public PostItemProfile(int id, int imageResId) {
        this.id = id;
        this.imageResId = imageResId;
    }

    public int getId() {
        return id;
    }

    public int getImageResId() {
        return imageResId;
    }
}
