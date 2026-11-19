package com.example.diyproject.ui.search;

public class SearchIdea {
    private final String title;
    private final int imageRes;

    public SearchIdea(String title, int imageRes) {
        this.title = title;
        this.imageRes = imageRes;
    }

    public String getTitle() { return title; }
    public int getImageRes() { return imageRes; }
}