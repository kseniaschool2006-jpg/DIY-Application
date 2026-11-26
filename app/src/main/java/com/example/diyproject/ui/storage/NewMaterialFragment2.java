package com.example.diyproject.ui.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentNewMaterial2Binding;

public class NewMaterialFragment2 extends Fragment {

    private FragmentNewMaterial2Binding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewMaterial2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnGoToEdit.setOnClickListener(v -> {
            NavHostFragment.findNavController(NewMaterialFragment2.this)
                    .navigate(R.id.action_newMaterialFragment2_to_editMaterialFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
