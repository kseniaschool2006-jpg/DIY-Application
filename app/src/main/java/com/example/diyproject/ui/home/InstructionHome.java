package com.example.diyproject.ui.home;

public class InstructionHome {

    private final long id;
    private final String title;
    private final int imageRes;

    public InstructionHome(long id, String title, int imageRes) {
        this.id = id;
        this.title = title;
        this.imageRes = imageRes;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public int getImageRes() { return imageRes; }
}