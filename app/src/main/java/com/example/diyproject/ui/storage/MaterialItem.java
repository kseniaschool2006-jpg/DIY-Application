package com.example.diyproject.ui.storage;

import java.io.Serializable;

public class MaterialItem implements Serializable {
    private String title;
    private String description;
    private String tag;
    private int iconRes;

    public MaterialItem(String title, String description, String tag, int iconRes) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.iconRes = iconRes;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTag() { return tag; }
    public int getIconRes() { return iconRes; }
}
