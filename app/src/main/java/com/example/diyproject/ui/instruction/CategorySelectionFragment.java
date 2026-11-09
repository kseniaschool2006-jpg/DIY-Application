package com.example.diyproject.ui.instruction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CategorySelectionFragment extends Fragment {

    private CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_selection, container, false);

        setupToolbar(view);

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


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CategoryAdapter(categories, preSelected);
        recyclerView.setAdapter(adapter);


        Button btnSelect = view.findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(v -> {
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

        return view;
    }

    private void setupToolbar(View view) {
        View toolbar = view.findViewById(R.id.customToolbar);
        ImageButton btnLeft = toolbar.findViewById(R.id.btnLeftAction);
        TextView tvTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        ImageButton btnRight = toolbar.findViewById(R.id.btnRightAction);

        tvTitle.setText("Выбор категории");
        btnLeft.setImageResource(R.drawable.ic_arrow_back);
        btnLeft.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        btnRight.setVisibility(View.GONE);
    }
}