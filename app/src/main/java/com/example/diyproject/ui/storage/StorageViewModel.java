package com.example.diyproject.ui.storage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class StorageViewModel extends ViewModel {
    private final MutableLiveData<List<MaterialItem>> materials = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<MaterialItem>> getMaterials() {
        return materials;
    }

    public void addMaterial(MaterialItem item) {
        List<MaterialItem> current = materials.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(item);
        materials.setValue(new ArrayList<>(current));
    }

    public void updateMaterial(int position, MaterialItem item) {
        List<MaterialItem> current = materials.getValue();
        if (current != null && position >= 0 && position < current.size()) {
            current.set(position, item);
            materials.setValue(new ArrayList<>(current));
        }
    }

    public void setMaterials(List<MaterialItem> items) {
        materials.setValue(new ArrayList<>(items));
    }
}
