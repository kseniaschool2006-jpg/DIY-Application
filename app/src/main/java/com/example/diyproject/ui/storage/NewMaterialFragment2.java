package com.example.diyproject.ui.storage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.diyproject.R;


public class NewMaterialFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_material2, container, false);

        Button btnGoToEdit = view.findViewById(R.id.btnGoToEdit);

        btnGoToEdit.setOnClickListener(v -> {
            NavHostFragment.findNavController(NewMaterialFragment2.this)
                    .navigate(R.id.action_newMaterialFragment2_to_editMaterialFragment);
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