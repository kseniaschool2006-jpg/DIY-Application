package com.example.diyproject.ui.storage;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diyproject.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


public class NewMaterialFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_material1, container, false);

        View cardGalleryInclude = view.findViewById(R.id.cardGallery);
        View cardCameraInclude = view.findViewById(R.id.cardCamera);
        View cardSmartInclude = view.findViewById(R.id.cardSmartDetector);
        View cardInternetInclude = view.findViewById(R.id.cardInternet);

        setupCard(cardGalleryInclude, R.drawable.ic_gallery, "Галерея", true);
        setupCard(cardCameraInclude, R.drawable.ic_camera, "Камера", false);
        setupCard(cardSmartInclude, R.drawable.ic_ai, "Умный детектор", false);
        setupCard(cardInternetInclude, R.drawable.ic_internet, "Интернет", false);



        setupToolbar(view);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLibrary);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<MaterialItem> libraryMaterials = new ArrayList<>();
        libraryMaterials.add(new MaterialItem("Материал", "Краткое описание", null, R.drawable.ic_material));
        libraryMaterials.add(new MaterialItem("Гвозди", "5см, тонкие, в кладовой", "мелкий ремонт", R.drawable.ic_material));
        libraryMaterials.add(new MaterialItem("Пряжа", "Зеленая пряжа, использовать крючек 4мм", "новогодние игрушки", R.drawable.ic_material));

        MaterialAdapter adapter = new MaterialAdapter(libraryMaterials, false, new MaterialAdapter.OnMaterialClickListener() {
            @Override
            public void onCardClick(MaterialItem item) {
                NavController navController = NavHostFragment.findNavController(NewMaterialFragment1.this);
                navController.navigate(R.id.action_newMaterialFragment1_to_newMaterialFragment2);
            }

            @Override
            public void onSettingsClick(MaterialItem item) {
                // Здесь ничего не делаем, так как showSettings = false
            }
        });

        recyclerView.setAdapter(adapter);




        TextView tvSeeAll = view.findViewById(R.id.tvSeeAll);
        String text = "Посмотреть все";
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        tvSeeAll.setText(spannable);
        return view;
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

        card.setOnClickListener(v -> {
            // Здесь можно открыть галерею, камеру и т.д.
        });


    }

    private void setupToolbar(View view) {
        View toolbar = view.findViewById(R.id.customToolbar);

        ImageButton btnLeft = toolbar.findViewById(R.id.btnLeftAction);
        ImageButton btnRight = toolbar.findViewById(R.id.btnRightAction);
        TextView tvTitle = toolbar.findViewById(R.id.tvToolbarTitle);

        tvTitle.setText("Добавить материал");

        btnLeft.setImageResource(R.drawable.ic_close);
        btnLeft.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.drawable.ic_question);
        btnRight.setOnClickListener(v -> {
        });
    }
}