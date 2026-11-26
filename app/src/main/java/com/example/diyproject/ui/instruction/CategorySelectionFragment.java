package com.example.diyproject.ui.instruction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentCategorySelectionBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategorySelectionFragment extends Fragment {

    private CategoryAdapter adapter;
    private FragmentCategorySelectionBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategorySelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();

        List<String> categories = new ArrayList<>();
        categories.add("Ремонт");
        categories.add("Рукоделие");
        categories.add("Реставрация");
        categories.add("Шитье");

        ArrayList<String> preSelected = new ArrayList<>();
        if (getArguments() != null) {
            CategorySelectionFragmentArgs args = CategorySelectionFragmentArgs.fromBundle(getArguments());
            String[] preSelectedArray = args.getSelectedThemes();
            if (preSelectedArray != null) {
                Collections.addAll(preSelected, preSelectedArray);
            }
        }

        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CategoryAdapter(categories, preSelected);
        binding.recyclerViewCategories.setAdapter(adapter);

        binding.btnSelect.setOnClickListener(v -> {
            List<String> selected = adapter.getSelectedCategories();
            if (!selected.isEmpty()) {
                Bundle result = new Bundle();
                result.putStringArrayList("selectedThemes", new ArrayList<>(selected));
                getParentFragmentManager().setFragmentResult("themeRequest", result);
                NavHostFragment.findNavController(CategorySelectionFragment.this).popBackStack();
            } else {
                Toast.makeText(requireContext(), "Выберите хотя бы одну тему", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupToolbar() {
        binding.customToolbar.tvToolbarTitle.setText("Выбор категории");
        binding.customToolbar.btnLeftAction.setImageResource(R.drawable.ic_arrow_back);
        binding.customToolbar.btnLeftAction.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        binding.customToolbar.btnRightAction.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
