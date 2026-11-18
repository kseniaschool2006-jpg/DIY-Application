package com.example.diyproject.ui.search;

public class SearchHistoryItem {

    private final long id;
    private final String title;
    private final int imageRes;

    public SearchHistoryItem(long id, String title, int imageRes) {
        this.id = id;
        this.title = title;
        this.imageRes = imageRes;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public int getImageRes() { return imageRes; }
}