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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.diyproject.R;
import com.example.diyproject.ui.common.SaveDraftBottomSheet;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class EditInstructionFragment extends Fragment {


    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private InstructionStepAdapter stepAdapter;
    private int pendingImagePosition = -1;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickStepImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null && stepAdapter != null && pendingImagePosition != -1) {
                    stepAdapter.setImageForStep(pendingImagePosition, uri);
                    pendingImagePosition = -1;
                }
            });

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


        // логика выбора темы\категории, позже решу как лучше обозвать
        View cardTheme = view.findViewById(R.id.cardTheme);
        ImageButton ivArrow = cardTheme.findViewById(R.id.ivArrow);

        cardTheme.setOnClickListener(v -> openCategorySelection(cardTheme));

        // Обработчик для стрелки
        ivArrow.setOnClickListener(v -> {
            openCategorySelection(cardTheme);
        });

        getParentFragmentManager().setFragmentResultListener("themeRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            ArrayList<String> selectedThemes = result.getStringArrayList("selectedThemes");
            if (selectedThemes != null && !selectedThemes.isEmpty()) {

                ChipGroup chipGroup = cardTheme.findViewById(R.id.chipGroup);
                chipGroup.setVisibility(View.VISIBLE);
                chipGroup.removeAllViews();


                for (String theme : selectedThemes) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_category, chipGroup, false);
                    chip.setText(theme);
                    chipGroup.addView(chip);
                }
            }
        });


        // карточка времени
        View cardTimeInclude = view.findViewById(R.id.cardTime);

        View materialCard = cardTimeInclude.findViewById(R.id.cardTimeRoot);

        TextView tvSelectedTime = cardTimeInclude.findViewById(R.id.tvSelectedTime);

        materialCard.setOnClickListener(v -> {
            ContextThemeWrapper wrapper = new ContextThemeWrapper(requireContext(), R.style.PopupMenuTheme);
            PopupMenu popupMenu = new PopupMenu(wrapper, v, Gravity.CENTER);
            popupMenu.getMenu().add("15 минут");
            popupMenu.getMenu().add("30 минут");
            popupMenu.getMenu().add("1 час");
            popupMenu.getMenu().add("2 часа");
            popupMenu.getMenu().add("3 часа");
            popupMenu.getMenu().add("Более 3 часов");

            popupMenu.setOnMenuItemClickListener(item -> {
                tvSelectedTime.setText(item.getTitle());
                return true;
            });
            popupMenu.show();
        });



        // Настройка RecyclerView для шагов
        RecyclerView rvInstructionSteps = view.findViewById(R.id.rvInstructionSteps);
        rvInstructionSteps.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<InstructionStep> steps = new ArrayList<>();
        // С самого начала один шаг для заполнения
        steps.add(new InstructionStep("", null));

        stepAdapter = new InstructionStepAdapter(steps, position -> {
            pendingImagePosition = position;
            pickStepImage.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        rvInstructionSteps.setAdapter(stepAdapter);

        TextView tvAddStep = view.findViewById(R.id.tvAddStep);
        tvAddStep.setOnClickListener(v -> {
            InstructionStep newStep = new InstructionStep("", null);
            steps.add(newStep);
            stepAdapter.notifyItemInserted(steps.size() - 1);
            rvInstructionSteps.smoothScrollToPosition(steps.size() - 1);
        });
    }

    private void openCategorySelection(View cardTheme) {
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

        NavHostFragment.findNavController(this).navigate(action);
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