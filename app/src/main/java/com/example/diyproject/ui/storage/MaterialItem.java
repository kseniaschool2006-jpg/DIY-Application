package com.example.diyproject.ui.storage;

import java.io.Serializable;

public class MaterialItem implements Serializable {
    private String title;
    private String description;
    private String tag;
    private int iconRes;
    private String imageUriString;

    public MaterialItem(String title, String description, String tag, int iconRes) {
        this(title, description, tag, iconRes, null);
    }

    public MaterialItem(String title, String description, String tag, int iconRes, String imageUriString) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.iconRes = iconRes;
        this.imageUriString = imageUriString;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTag() { return tag; }
    public int getIconRes() { return iconRes; }

    public String getImageUriString() { return imageUriString; }
    public void setImageUriString(String imageUriString) { this.imageUriString = imageUriString; }
}
