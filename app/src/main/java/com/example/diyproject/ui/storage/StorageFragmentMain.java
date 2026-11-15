package com.example.diyproject.ui.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
    private MaterialAdapter adapter;
    private StorageViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStorageMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализируем ViewModel на уровне Activity, чтобы данные сохранялись между переходами
        viewModel = new ViewModelProvider(requireActivity()).get(StorageViewModel.class);

        List<String> categories = loadCategoriesFromDatabase();
        setupTabs(categories);
        setupRecyclerView();

        // Слушатель добавления материала из библиотеки или выбора
        getParentFragmentManager().setFragmentResultListener("addMaterialToStorage", getViewLifecycleOwner(), (requestKey, result) -> {
            MaterialItem newItem = (MaterialItem) result.getSerializable("materialItem");
            if (newItem != null) {
                viewModel.addMaterial(newItem);
            }
        });

        // Слушатель сохранения/редактирования материала из EditMaterialFragment
        getParentFragmentManager().setFragmentResultListener("editMaterialSaveRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            int position = result.getInt("position", -1);
            String title = result.getString("title");
            String description = result.getString("description");
            String tag = result.getString("tag");
            int iconRes = result.getInt("iconRes", 0);
            String imageUriString = result.getString("imageUriString");

            List<MaterialItem> currentList = viewModel.getMaterials().getValue();
            if (currentList != null && position >= 0 && position < currentList.size()) {
                MaterialItem oldItem = currentList.get(position);
                String newTitle = (title != null && !title.isEmpty()) ? title : oldItem.getTitle();
                String newDesc = (description != null) ? description : oldItem.getDescription();
                String newTag = tag;
                int newIcon = (iconRes != 0) ? iconRes : oldItem.getIconRes();
                String newImageUri = (imageUriString != null) ? imageUriString : oldItem.getImageUriString();

                MaterialItem updatedItem = new MaterialItem(newTitle, newDesc, newTag, newIcon, newImageUri);
                viewModel.updateMaterial(position, updatedItem);
            } else if (title != null && !title.isEmpty()) {
                int newIcon = (iconRes != 0) ? iconRes : R.drawable.ic_material;
                MaterialItem newItem = new MaterialItem(title, description != null ? description : "", tag, newIcon, imageUriString);
                viewModel.addMaterial(newItem);
            }
        });

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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (viewModel.getMaterials().getValue() == null || viewModel.getMaterials().getValue().isEmpty()) {
            List<MaterialItem> initial = new ArrayList<>();
            initial.add(new MaterialItem("Материал", "Краткое описание", null, R.drawable.ic_material));
            initial.add(new MaterialItem("Гвозди", "5см, тонкие", "мелкийремонт", R.drawable.ic_material));
            initial.add(new MaterialItem("Пряжа", "Зеленая пряжа", "новогодниеигрушки", R.drawable.ic_material));
            viewModel.setMaterials(initial);
        }

        viewModel.getMaterials().observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                adapter = new MaterialAdapter(materials, true, false, new MaterialAdapter.OnMaterialClickListener() {
                    @Override
                    public void onCardClick(MaterialItem item) {}

                    @Override
                    public void onSettingsClick(MaterialItem item) {
                        int index = materials.indexOf(item);
                        StorageFragmentMainDirections.ActionStorageFragmentMainToEditMaterialFragment action =
                                StorageFragmentMainDirections.actionStorageFragmentMainToEditMaterialFragment();
                        action.setMaterialTitle(item.getTitle());
                        action.setMaterialDescription(item.getDescription());
                        action.setMaterialTag(item.getTag());
                        action.setMaterialIconRes(item.getIconRes());
                        action.setMaterialImageUriString(item.getImageUriString());
                        action.setMaterialPosition(index);

                        NavHostFragment.findNavController(StorageFragmentMain.this).navigate(action);
                    }
                });
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
