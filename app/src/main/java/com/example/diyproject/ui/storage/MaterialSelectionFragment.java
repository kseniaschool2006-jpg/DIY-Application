package com.example.diyproject.ui.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaterialSelectionFragment extends Fragment {

    private MaterialAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_material_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);

        ArrayList<String> preSelected = new ArrayList<>();
        if (getArguments() != null) {
            MaterialSelectionFragmentArgs args = MaterialSelectionFragmentArgs.fromBundle(getArguments());
            String[] preSelectedArray = args.getSelectedMaterials();
            if (preSelectedArray != null) {
                Collections.addAll(preSelected, preSelectedArray);
            }
        }

        List<MaterialItem> materials = new ArrayList<>();
        materials.add(new MaterialItem("Пряжа", "Зеленая, 4мм", null, R.drawable.ic_material));
        materials.add(new MaterialItem("Гвозди", "5см, тонкие", null, R.drawable.ic_material));
        materials.add(new MaterialItem("Нитки", "Белые, хлопок", null, R.drawable.ic_material));
        materials.add(new MaterialItem("Бисер", "Мелкий, красный", null, R.drawable.ic_material));
        materials.add(new MaterialItem("Крючок", "3мм", null, R.drawable.ic_material));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMaterials);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new MaterialAdapter(materials, false, true, preSelected, new MaterialAdapter.OnMaterialClickListener() {
            @Override
            public void onCardClick(MaterialItem item) {}

            @Override
            public void onSettingsClick(MaterialItem item) {}
        });
        recyclerView.setAdapter(adapter);

        Button btnSelect = view.findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(v -> {
            List<MaterialItem> selected = adapter.getSelectedItems();
            if (!selected.isEmpty()) {
                ArrayList<String> titles = new ArrayList<>();
                ArrayList<MaterialItem> selectedList = new ArrayList<>(selected);
                for (MaterialItem m : selected) {
                    titles.add(m.getTitle());
                }

                Bundle result = new Bundle();
                result.putStringArrayList("selectedMaterials", titles);
                result.putSerializable("selectedMaterialsList", selectedList);
                getParentFragmentManager().setFragmentResult("materialRequest", result);

                NavHostFragment.findNavController(MaterialSelectionFragment.this).popBackStack();
            } else {
                Toast.makeText(requireContext(), "Выберите хотя бы один материал", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupToolbar(View view) {
        View toolbar = view.findViewById(R.id.customToolbar);
        ImageButton btnLeft = toolbar.findViewById(R.id.btnLeftAction);
        TextView tvTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        ImageButton btnRight = toolbar.findViewById(R.id.btnRightAction);

        tvTitle.setText("Выбор материалов");
        btnLeft.setImageResource(R.drawable.ic_arrow_back);
        btnLeft.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        btnRight.setVisibility(View.GONE);
    }
}
