package com.example.diyproject.ui.instruction;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.diyproject.ui.storage.MaterialItem;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EditInstructionViewModel extends ViewModel {

    private final MutableLiveData<Uri> imageUri = new MutableLiveData<>();
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> description = new MutableLiveData<>("");
    private final MutableLiveData<String> selectedTime = new MutableLiveData<>();

    private final MutableLiveData<List<String>> selectedCategories = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Set<String>> hashtags = new MutableLiveData<>(new LinkedHashSet<>());
    private final MutableLiveData<ArrayList<String>> selectedMaterialTitles = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<MaterialItem>> selectedMaterialItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<InstructionStep>> instructionSteps = new MutableLiveData<>(new ArrayList<>());

    public LiveData<Uri> getImageUri() { return imageUri; }
    public void setImageUri(Uri uri) { imageUri.setValue(uri); }

    public LiveData<String> getTitle() { return title; }
    public void setTitle(String text) { title.setValue(text); }

    public LiveData<String> getDescription() { return description; }
    public void setDescription(String text) { description.setValue(text); }

    public LiveData<String> getSelectedTime() { return selectedTime; }
    public void setSelectedTime(String time) { selectedTime.setValue(time); }

    public LiveData<List<String>> getSelectedCategories() { return selectedCategories; }
    public void setSelectedCategories(List<String> categories) { selectedCategories.setValue(categories); }

    public LiveData<Set<String>> getHashtags() { return hashtags; }
    public void setHashtags(Set<String> tags) { hashtags.setValue(tags); }

    public LiveData<ArrayList<String>> getSelectedMaterialTitles() { return selectedMaterialTitles; }
    public void setSelectedMaterialTitles(ArrayList<String> titles) { selectedMaterialTitles.setValue(titles); }

    public LiveData<List<MaterialItem>> getSelectedMaterialItems() { return selectedMaterialItems; }
    public void setSelectedMaterialItems(List<MaterialItem> materials) { selectedMaterialItems.setValue(materials); }

    public LiveData<List<InstructionStep>> getInstructionSteps() { return instructionSteps; }
    public void setInstructionSteps(List<InstructionStep> steps) { instructionSteps.setValue(steps); }
}
