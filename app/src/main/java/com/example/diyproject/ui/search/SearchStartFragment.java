package com.example.diyproject.ui.search;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diyproject.R;

public class SearchStartFragment extends Fragment {

    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    // позже поиск по фото
                    Toast.makeText(requireContext(), "Фото получено", Toast.LENGTH_SHORT).show();
                }
            });

    public SearchStartFragment() {
        super(R.layout.fragment_search_start);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.search_bar).setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_searchStart_to_searchHistory));
        
    }
}