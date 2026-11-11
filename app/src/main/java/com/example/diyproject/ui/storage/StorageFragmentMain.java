package com.example.diyproject.ui.storage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.diyproject.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class StorageFragmentMain extends Fragment {


    private TabLayout tabLayout;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage_main, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        List<String> categories = loadCategoriesFromDatabase();

        setupTabs(categories);

        setupRecyclerView();

        FrameLayout fabMain = view.findViewById(R.id.fabMain);
        fabMain.setOnClickListener(v -> {
            // Переход на экран добавления материала
            NavController navController = NavHostFragment.findNavController(StorageFragmentMain.this);
            navController.navigate(R.id.action_storageFragmentMain_to_newMaterialFragment1);
        });

        return view;
    }

    private List<String> loadCategoriesFromDatabase() {
        // Пока тестовые данные
        List<String> categories = new ArrayList<>();
        categories.add("Все");
        categories.add("Руководство");
        categories.add("Ремонт");
        categories.add("Сад");
        return categories;
    }

    private void setupTabs(List<String> categories) {

        tabLayout.removeAllTabs();

        for (String category : categories) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(category);
            tabLayout.addTab(tab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedCategory = tab.getText().toString();
                // TODO: отфильтровать список карточек по выбранной категории

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void setupRecyclerView() {
        List<MaterialItem> materials = new ArrayList<>();
        materials.add(new MaterialItem("Материал", "Краткое описание", null, R.drawable.ic_material));
        materials.add(new MaterialItem("Гвозди", "5см, тонкие", "мелкий ремонт", R.drawable.ic_material));
        materials.add(new MaterialItem("Пряжа", "Зеленая пряжа", "новогодние игрушки", R.drawable.ic_material));

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        MaterialAdapter adapter = new MaterialAdapter(materials, true,  false,  new MaterialAdapter.OnMaterialClickListener() {
            @Override
            public void onCardClick(MaterialItem item) {
                // ПЕРЕХОД НА ЭКРАН РЕДАКТИРОВАНИЯ С ПЕРЕДАЧЕЙ ДАННЫХ
            }

            @Override
            public void onSettingsClick(MaterialItem item) {
                // ПЕРЕХОД НА ЭКРАН РЕДАКТИРОВАНИЯ С ПЕРЕДАЧЕЙ ДАННЫХ

            }
        });

        recyclerView.setAdapter(adapter);
    }
}
