package com.example.diyproject.ui.instruction;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diyproject.R;
import com.example.diyproject.ui.common.SaveDraftBottomSheet;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class EditInstructionFragment extends Fragment {


    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {

                        ImageView ivInstructionImage = requireView().findViewById(R.id.ivInstructionImage);
                        loadPreview(uri, ivInstructionImage);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit_instruction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        подставляю фото принятое из галереи
        ImageView ivInstructionImage = view.findViewById(R.id.ivInstructionImage);

        if (getArguments() != null) {
            String imageUriString = getArguments().getString("instructionImageUri");
            if (imageUriString != null && !imageUriString.isEmpty()) {

                Uri imageUri = Uri.parse(imageUriString);
                loadPreview(imageUri, ivInstructionImage);
            }
        }

//        открытие галереи
        View frameImagePicker = view.findViewById(R.id.frameImagePicker);
        frameImagePicker.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
        });


//        закрытие на крестик
        View toolbar = view.findViewById(R.id.customToolbar);

        ImageButton btnClose = toolbar.findViewById(R.id.btnLeftAction);

        btnClose.setOnClickListener(v -> {
            SaveDraftBottomSheet bottomSheet = new SaveDraftBottomSheet();
            bottomSheet.setListener(new SaveDraftBottomSheet.OnDraftActionListener() {
                @Override
                public void onDelete() {

                    NavHostFragment.findNavController(EditInstructionFragment.this).popBackStack();
                }

                @Override
                public void onSaveDraft() {
                    // TODO: сохранить данные потом
                    NavHostFragment.findNavController(EditInstructionFragment.this).popBackStack();
                }
            });
            bottomSheet.show(getParentFragmentManager(), "SaveDraftBottomSheet");
        });


        // Находим карточку выбора темы
        View cardTheme = view.findViewById(R.id.cardTheme);
        TextView tvThemeTitle = cardTheme.findViewById(R.id.tvTitle);
        tvThemeTitle.setText("Выбор темы");

        cardTheme.setOnClickListener(v -> {

            ChipGroup chipGroup = cardTheme.findViewById(R.id.chipGroup);
            ArrayList<String> currentThemes = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                currentThemes.add(chip.getText().toString());
            }

            EditInstructionFragmentDirections.ActionEditInstructionFragmentToCategorySelectionFragment action =
                    EditInstructionFragmentDirections.actionEditInstructionFragmentToCategorySelectionFragment();

            if (currentThemes.isEmpty()) {
                action.setSelectedThemes(null);
            } else {
                action.setSelectedThemes(currentThemes.toArray(new String[0]));
            }

            NavHostFragment.findNavController(EditInstructionFragment.this).navigate(action);
        });

        getParentFragmentManager().setFragmentResultListener("themeRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            ArrayList<String> selectedThemes = result.getStringArrayList("selectedThemes");
            if (selectedThemes != null && !selectedThemes.isEmpty()) {

                ChipGroup chipGroup = cardTheme.findViewById(R.id.chipGroup);
                chipGroup.setVisibility(View.VISIBLE);
                chipGroup.removeAllViews();


                for (String theme : selectedThemes) {

                    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(requireContext(), R.style.ChipStyle_Category);

                    Chip chip = new Chip(contextThemeWrapper);
                    chip.setText(theme);

                    chip.setChipIconResource(R.drawable.ic_star);

                    chipGroup.addView(chip);
                }
            }
        });

    }


    private void loadPreview(Uri uri, ImageView imageView) {
        String mimeType = requireContext().getContentResolver().getType(uri);

        if (mimeType != null && mimeType.startsWith("video/")) {

            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(requireContext(), uri);
                Bitmap firstFrame = retriever.getFrameAtTime(0);
                retriever.release();

                if (firstFrame != null) {
                    imageView.setImageBitmap(firstFrame);
                } else {
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            imageView.setImageURI(uri);
        }
    }


}