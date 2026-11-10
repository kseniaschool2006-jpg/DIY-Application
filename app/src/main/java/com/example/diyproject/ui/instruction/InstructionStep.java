package com.example.diyproject.ui.instruction;

import android.net.Uri;

public class InstructionStep {
    private String title;
    private String subtitle;
    private Uri imageUri;

    public InstructionStep(String title, Uri imageUri) {
        this.title = title;
        this.subtitle = "";
        this.imageUri = imageUri;
    }

    public String getTitle() { return title != null ? title : ""; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle != null ? subtitle : ""; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public Uri getImageUri() { return imageUri; }
    public void setImageUri(Uri imageUri) { this.imageUri = imageUri; }
}
