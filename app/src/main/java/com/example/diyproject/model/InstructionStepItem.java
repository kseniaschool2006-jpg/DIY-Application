package com.example.diyproject.model;

import java.util.ArrayList;
import java.util.List;

public class InstructionStepItem {

    private  int stepNumber;
    private  String title;
    private  String description;
    private  List<StepMediaItem> mediaList;

    public InstructionStepItem(int stepNumber, String title, String description, List<StepMediaItem> mediaList) {
        this.stepNumber = stepNumber;
        this.title = title;
        this.description = description;
        this.mediaList = mediaList != null ? mediaList : new ArrayList<>();
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<StepMediaItem> getMediaList() {
        return mediaList;
    }
}
