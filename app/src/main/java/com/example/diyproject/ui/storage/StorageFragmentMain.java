package com.example.diyproject.ui.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentStorageMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class StorageFragmentMain extends Fragment {

    private FragmentStorageMainBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStorageMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> categories = loadCategoriesFromDatabase();
        setupTabs(categories);
        setupRecyclerView();

        binding.fabMain.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(StorageFragmentMain.this);
            navController.navigate(R.id.action_storageFragmentMain_to_newMaterialFragment1);
        });
    }

    private List<String> loadCategoriesFromDatabase() {
        List<String> categories = new ArrayList<>();
        categories.add("Все");
        categories.add("Руководство");
        categories.add("Ремонт");
        categories.add("Сад");
        return categories;
    }

    private void setupTabs(List<String> categories) {
        binding.tabLayout.removeAllTabs();

        for (String category : categories) {
            TabLayout.Tab tab = binding.tabLayout.newTab();
            tab.setText(category);
            binding.tabLayout.addTab(tab);
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedCategory = tab.getText().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        List<MaterialItem> materials = new ArrayList<>();
        materials.add(new MaterialItem("Материал", "Краткое описание", null, R.drawable.ic_material));
        materials.add(new MaterialItem("Гвозди", "5см, тонкие", "мелкий ремонт", R.drawable.ic_material));
        materials.add(new MaterialItem("Пряжа", "Зеленая пряжа", "новогодние игрушки", R.drawable.ic_material));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        MaterialAdapter adapter = new MaterialAdapter(materials, true, false, new MaterialAdapter.OnMaterialClickListener() {
            @Override
            public void onCardClick(MaterialItem item) {}

            @Override
            public void onSettingsClick(MaterialItem item) {}
        });

        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
