package com.example.diyproject.ui.storage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.diyproject.R;

public class EditMaterialFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_material, container, false);

        setupToolbarForEditScreen(view);

        View cardCategory = view.findViewById(R.id.cardCategory);
        View btnCategory = cardCategory.findViewById(R.id.ivArrow);

        cardCategory.setOnClickListener(v -> {
            NavHostFragment.findNavController(EditMaterialFragment.this)
                    .navigate(R.id.action_editMaterialFragment_to_categorySelectionFragment);
        });

        btnCategory.setOnClickListener(v -> {
            NavHostFragment.findNavController(EditMaterialFragment.this)
                    .navigate(R.id.action_editMaterialFragment_to_categorySelectionFragment);
        });

        return view;
    }

    private void setupToolbarForEditScreen(View view) {
        View toolbar = view.findViewById(R.id.customToolbar);
        ImageButton btnLeft = toolbar.findViewById(R.id.btnLeftAction);
        ImageButton btnRight = toolbar.findViewById(R.id.btnRightAction);
        TextView tvTitle = toolbar.findViewById(R.id.tvToolbarTitle);

        tvTitle.setText("Редактировать материал");

        btnLeft.setImageResource(R.drawable.ic_arrow_back);
        btnLeft.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnRight.setVisibility(View.GONE);
    }
}