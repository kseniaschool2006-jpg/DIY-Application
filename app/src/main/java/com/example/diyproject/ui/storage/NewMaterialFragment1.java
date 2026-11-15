package com.example.diyproject.ui.storage;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentNewMaterial1Binding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class NewMaterialFragment1 extends Fragment {

    private FragmentNewMaterial1Binding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewMaterial1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupCard(binding.cardGallery.getRoot(), R.drawable.ic_gallery, "Галерея", true);
        setupCard(binding.cardCamera.getRoot(), R.drawable.ic_camera, "Камера", false);
        setupCard(binding.cardSmartDetector.getRoot(), R.drawable.ic_ai, "Умный детектор", false);
        setupCard(binding.cardInternet.getRoot(), R.drawable.ic_internet, "Интернет", false);

        setupToolbar();

        binding.recyclerViewLibrary.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<MaterialItem> libraryMaterials = new ArrayList<>();
        libraryMaterials.add(new MaterialItem("Материал", "Краткое описание", null, R.drawable.ic_material));
        libraryMaterials.add(new MaterialItem("Гвозди", "5см, тонкие, в кладовой", "мелкий ремонт", R.drawable.ic_material));
        libraryMaterials.add(new MaterialItem("Пряжа", "Зеленая пряжа, использовать крючек 4мм", "новогодние игрушки", R.drawable.ic_material));

        MaterialAdapter adapter = new MaterialAdapter(libraryMaterials, false, false, new MaterialAdapter.OnMaterialClickListener() {
            @Override
            public void onCardClick(MaterialItem item) {
                NavController navController = NavHostFragment.findNavController(NewMaterialFragment1.this);
                navController.navigate(R.id.action_newMaterialFragment1_to_newMaterialFragment2);
            }

            @Override
            public void onSettingsClick(MaterialItem item) {}
        });

        binding.recyclerViewLibrary.setAdapter(adapter);

        String text = "Посмотреть все";
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        binding.tvSeeAll.setText(spannable);
    }

    private void setupCard(View includeView, int iconRes, String title, boolean isActive) {
        if (includeView == null) return;
        MaterialCardView card = (MaterialCardView) includeView;
        ImageView icon = includeView.findViewById(R.id.ivIcon);
        TextView text = includeView.findViewById(R.id.tvTitle);

        if (icon != null) {
            icon.setImageResource(iconRes);
        }
        if (text != null) {
            text.setText(title);
        }

        int greenColor = ContextCompat.getColor(requireContext(), R.color.color_green);
        int whiteColor = ContextCompat.getColor(requireContext(), R.color.color_white);
        int beigeDarkColor = ContextCompat.getColor(requireContext(), R.color.color_beige_dark);

        if (isActive) {
            card.setCardBackgroundColor(greenColor);
            card.setStrokeWidth(0);
            if (icon != null) {
                icon.setImageTintList(ColorStateList.valueOf(whiteColor));
            }
            if (text != null) {
                text.setTextColor(whiteColor);
            }
        } else {
            card.setCardBackgroundColor(whiteColor);
            card.setStrokeWidth(1);
            card.setStrokeColor(greenColor);
            if (icon != null) {
                icon.setImageTintList(ColorStateList.valueOf(beigeDarkColor));
            }
            if (text != null) {
                text.setTextColor(beigeDarkColor);
            }
        }
    }

    private void setupToolbar() {
        binding.customToolbar.tvToolbarTitle.setText("Добавить материал");
        binding.customToolbar.btnLeftAction.setImageResource(R.drawable.ic_close);
        binding.customToolbar.btnLeftAction.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        binding.customToolbar.btnRightAction.setVisibility(View.VISIBLE);
        binding.customToolbar.btnRightAction.setImageResource(R.drawable.ic_question);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
