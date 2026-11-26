package com.example.diyproject.model;

public class StepMediaItem {

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    private  String uri;
    private  int type;
    private  int durationSeconds;

    public StepMediaItem(String uri, int type, int durationSeconds) {
        this.uri = uri;
        this.type = type;
        this.durationSeconds = durationSeconds;
    }

    public String getUri() {
        return uri;
    }

    public int getType() {
        return type;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public boolean isVideo() {
        return type == TYPE_VIDEO;
    }
}
