package com.example.diyproject.model;

public class AlbumItemProfile {

    public int id;
    public String title;
    public int count;
    public int cover1, cover2, cover3;
    public boolean isAddButton;

    public AlbumItemProfile(int id, String title, int count, int cover1, int cover2, int cover3, boolean isAddButton) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.cover1 = cover1;
        this.cover2 = cover2;
        this.cover3 = cover3;
        this.isAddButton = isAddButton;
    }

    public AlbumItemProfile(int id, String title, int count, int cover1, int cover2, int cover3) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.cover1 = cover1;
        this.cover2 = cover2;
        this.cover3 = cover3;
    }

    public AlbumItemProfile(boolean isAddButton) {
        this.isAddButton = isAddButton;
    }
}
