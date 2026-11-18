package com.example.diyproject.ui.home;

public class InstructionHome {

    private final long id;
    private final String title;
    private final int imageRes;
    private final String tags;

    public InstructionHome(long id, String title, int imageRes) {
        this(id, title, imageRes, "");
    }

    public InstructionHome(long id, String title, int imageRes, String tags) {
        this.id = id;
        this.title = title;
        this.imageRes = imageRes;
        this.tags = tags;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public int getImageRes() { return imageRes; }
    public String getTags() { return tags; }
}