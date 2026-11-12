package com.example.diyproject.ui.storage;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EditMaterialViewModel extends ViewModel {

    private final MutableLiveData<Uri> imageUri = new MutableLiveData<>();
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> description = new MutableLiveData<>("");

    private final MutableLiveData<List<String>> selectedCategories = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Set<String>> hashtags = new MutableLiveData<>(new LinkedHashSet<>());

    public LiveData<Uri> getImageUri() { return imageUri; }
    public void setImageUri(Uri uri) { imageUri.setValue(uri); }

    public LiveData<String> getTitle() { return title; }
    public void setTitle(String text) { title.setValue(text); }

    public LiveData<String> getDescription() { return description; }
    public void setDescription(String text) { description.setValue(text); }

    public LiveData<List<String>> getSelectedCategories() { return selectedCategories; }
    public void setSelectedCategories(List<String> categories) { selectedCategories.setValue(categories); }

    public LiveData<Set<String>> getHashtags() { return hashtags; }
    public void setHashtags(Set<String> tags) { hashtags.setValue(tags); }
}
