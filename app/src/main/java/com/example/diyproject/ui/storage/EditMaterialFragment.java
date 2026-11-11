package com.example.diyproject.ui.storage;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diyproject.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditMaterialFragment extends Fragment {

    private final Set<String> hashtagSet = new LinkedHashSet<>();
    private boolean isUpdatingDescription = false;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private int currentIconRes = R.drawable.ic_material;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null && getView() != null) {
                        ImageView ivMaterialImage = getView().findViewById(R.id.ivMaterialImage);
                        loadPreview(uri, ivMaterialImage);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_material, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbarForEditScreen(view);

        // Галерея выбора фото
        View frameImagePicker = view.findViewById(R.id.frameImagePicker);
        if (frameImagePicker != null) {
            frameImagePicker.setOnClickListener(v -> {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            });
        }

        // Выбор категории
        View cardCategory = view.findViewById(R.id.cardCategory);
        TextView tvCategoryTitle = cardCategory.findViewById(R.id.tvTitle);
        if (tvCategoryTitle != null) {
            tvCategoryTitle.setText("Выбор категории");
        }

        ImageButton ivArrow = cardCategory.findViewById(R.id.ivArrow);
        cardCategory.setOnClickListener(v -> openCategorySelection(cardCategory));
        if (ivArrow != null) {
            ivArrow.setOnClickListener(v -> openCategorySelection(cardCategory));
        }

        getParentFragmentManager().setFragmentResultListener("themeRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            ArrayList<String> selectedThemes = result.getStringArrayList("selectedThemes");
            if (selectedThemes != null && !selectedThemes.isEmpty()) {
                ChipGroup chipGroup = cardCategory.findViewById(R.id.chipGroup);
                chipGroup.setVisibility(View.VISIBLE);
                chipGroup.removeAllViews();

                for (String theme : selectedThemes) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_category, chipGroup, false);
                    chip.setText(theme);
                    chipGroup.addView(chip);
                }
            }
        });

        View cardTitle = view.findViewById(R.id.cardTitle);
        EditText etTitle = cardTitle.findViewById(R.id.etTitle);
        TextView tvTitleCounter = cardTitle.findViewById(R.id.tvTitleCounter);

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

        View cardDescription = view.findViewById(R.id.cardDescription);
        EditText etDescription = cardDescription.findViewById(R.id.etDescription);
        TextView tvAddHashtag = cardDescription.findViewById(R.id.tvAddHashtag);
        ChipGroup chipGroupTags = cardDescription.findViewById(R.id.chipGroupTags);

        tvAddHashtag.setOnClickListener(v -> showAddHashtagDialog(chipGroupTags));

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingDescription) return;

                TextView tvDescCounter = cardDescription.findViewById(R.id.tvDescCounter);
                tvDescCounter.setText(s.length() + "/400");

                String currentText = s.toString();
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

        int targetPosition = -1;
        if (getArguments() != null) {
            EditMaterialFragmentArgs args = EditMaterialFragmentArgs.fromBundle(getArguments());
            String title = args.getMaterialTitle();
            String description = args.getMaterialDescription();
            String tag = args.getMaterialTag();
            int iconRes = args.getMaterialIconRes();
            targetPosition = args.getMaterialPosition();

            if (title != null && !title.isEmpty()) {
                etTitle.setText(title);
            }
            if (description != null && !description.isEmpty()) {
                etDescription.setText(description);
            }
            if (iconRes != 0) {
                currentIconRes = iconRes;
                ImageView ivMaterialImage = view.findViewById(R.id.ivMaterialImage);
                if (ivMaterialImage != null) {
                    ivMaterialImage.setImageResource(iconRes);
                }
            }
            if (tag != null && !tag.isEmpty()) {
                ChipGroup chipGroup = cardCategory.findViewById(R.id.chipGroup);
                chipGroup.setVisibility(View.VISIBLE);
                chipGroup.removeAllViews();
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_category, chipGroup, false);
                chip.setText(tag);
                chipGroup.addView(chip);
            }
        }

        final int position = targetPosition;
        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            String updatedTitle = etTitle.getText().toString().trim();
            String updatedDesc = etDescription.getText().toString().trim();

            String updatedTag = null;
            ChipGroup chipGroup = cardCategory.findViewById(R.id.chipGroup);
            if (chipGroup.getChildCount() > 0) {
                View child = chipGroup.getChildAt(0);
                if (child instanceof Chip) {
                    updatedTag = ((Chip) child).getText().toString();
                }
            }

            Bundle result = new Bundle();
            result.putInt("position", position);
            result.putString("title", updatedTitle);
            result.putString("description", updatedDesc);
            result.putString("tag", updatedTag);
            result.putInt("iconRes", currentIconRes);

            getParentFragmentManager().setFragmentResult("editMaterialSaveRequest", result);
            NavHostFragment.findNavController(EditMaterialFragment.this).popBackStack();
        });
    }

    private void openCategorySelection(View cardCategory) {
        ChipGroup chipGroup = cardCategory.findViewById(R.id.chipGroup);
        ArrayList<String> currentThemes = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            currentThemes.add(chip.getText().toString());
        }

        EditMaterialFragmentDirections.ActionEditMaterialFragmentToCategorySelectionFragment action =
                EditMaterialFragmentDirections.actionEditMaterialFragmentToCategorySelectionFragment();

        if (currentThemes.isEmpty()) {
            action.setSelectedThemes(null);
        } else {
            action.setSelectedThemes(currentThemes.toArray(new String[0]));
        }

        NavHostFragment.findNavController(this).navigate(action);
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

        if (btnRight != null) {
            btnRight.setVisibility(View.GONE);
        }
    }
}
