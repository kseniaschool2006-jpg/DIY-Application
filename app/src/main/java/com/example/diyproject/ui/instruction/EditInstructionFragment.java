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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyproject.R;
import com.example.diyproject.ui.common.SaveDraftBottomSheet;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditInstructionFragment extends Fragment {

    private final Set<String> hashtagSet = new LinkedHashSet<>();
    private boolean isUpdatingDescription = false;

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


        // редактор названия (100 символов)
        View titleInclude = view.findViewById(R.id.cardTitle);
        if (titleInclude != null) {
            EditText etTitle = titleInclude.findViewById(R.id.etTitle);
            TextView tvTitleCounter = titleInclude.findViewById(R.id.tvTitleCounter);

            etTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    tvTitleCounter.setText(s.length() + "/100");
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }




        View descInclude = view.findViewById(R.id.cardDescription);
        EditText etDescription = descInclude.findViewById(R.id.etDescription);
        TextView tvAddHashtag = descInclude.findViewById(R.id.tvAddHashtag);
        ChipGroup chipGroupTags = descInclude.findViewById(R.id.chipGroupTags);

        // Кнопка "+ Добавить хештег" открывает диалог
        tvAddHashtag.setOnClickListener(v -> showAddHashtagDialog(chipGroupTags));

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingDescription) return;

                TextView tvDescCounter = descInclude.findViewById(R.id.tvDescCounter);
                tvDescCounter.setText(s.length() + "/400");

                String currentText = s.toString();
                // Ищем завершенные хештеги вида #слово (только когда после него введен пробел/перенос \s)
                Pattern pattern = Pattern.compile("#([a-zA-Zа-яА-Я0-9_]+)\\s");
                Matcher matcher = pattern.matcher(currentText);

                boolean found = false;
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    found = true;
                    String foundTag = matcher.group(1);
                    if (foundTag != null && !foundTag.trim().isEmpty()) {
                        hashtagSet.add(foundTag.trim());
                    }
                    matcher.appendReplacement(sb, "");
                }

                if (found) {
                    matcher.appendTail(sb);
                    String updatedText = sb.toString();

                    isUpdatingDescription = true;
                    etDescription.setText(updatedText);
                    etDescription.setSelection(updatedText.length());
                    isUpdatingDescription = false;

                    refreshHashtagChips(chipGroupTags);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
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

    private void addHashtag(String tag, ChipGroup chipGroup) {
        if (tag == null) return;
        String cleanTag = tag.replace("#", "").trim();
        if (!cleanTag.isEmpty()) {
            hashtagSet.add(cleanTag);
            refreshHashtagChips(chipGroup);
        }
    }

    private void refreshHashtagChips(ChipGroup chipGroup) {
        int childCount = chipGroup.getChildCount();
        if (childCount > 1) {
            chipGroup.removeViews(1, childCount - 1);
        }

        for (String tag : hashtagSet) {
            TextView tagView = (TextView) getLayoutInflater().inflate(R.layout.item_hashtag, chipGroup, false);
            tagView.setText(getString(R.string.tag_hash_format, tag));
            tagView.setOnClickListener(v -> {
                hashtagSet.remove(tag);
                refreshHashtagChips(chipGroup);
            });
            chipGroup.addView(tagView);
        }
    }

    private void showAddHashtagDialog(ChipGroup chipGroupTags) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_hashtag, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        AutoCompleteTextView etNewTag = dialogView.findViewById(R.id.etNewTag);
        Button btnAddTag = dialogView.findViewById(R.id.btnAddTag);

        String[] popularTags = {"вязание", "ремонт", "игрушки", "пряжа", "шитье", "реставрация", "поделки", "новогодние"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.item_dropdown_tag, popularTags);
        etNewTag.setAdapter(adapter);

        etNewTag.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTag = (String) parent.getItemAtPosition(position);
            etNewTag.setText(selectedTag);
            etNewTag.setSelection(selectedTag.length());
        });

        btnAddTag.setOnClickListener(v -> {
            String newTag = etNewTag.getText().toString().trim();
            if (!newTag.isEmpty()) {
                addHashtag(newTag, chipGroupTags);
                dialog.dismiss();
            } else {
                Toast.makeText(requireContext(), "Введите тег", Toast.LENGTH_SHORT).show();
            }
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