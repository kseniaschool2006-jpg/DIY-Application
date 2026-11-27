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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diyproject.R;
import com.example.diyproject.databinding.DialogAddHashtagBinding;
import com.example.diyproject.databinding.FragmentEditMaterialBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditMaterialFragment extends Fragment {

    private final Set<String> hashtagSet = new LinkedHashSet<>();
    private boolean isUpdatingDescription = false;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private int currentIconRes = R.drawable.ic_material;
    private Uri selectedImageUri = null;

    private EditMaterialViewModel viewModel;
    private FragmentEditMaterialBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null && binding != null) {
                        selectedImageUri = uri;
                        loadPreview(uri, binding.ivMaterialImage);
                        if (viewModel != null) {
                            viewModel.setImageUri(uri);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditMaterialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EditMaterialViewModel.class);

        setupToolbarForEditScreen();

        viewModel.getImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                loadPreview(uri, binding.ivMaterialImage);
            }
        });

        binding.frameImagePicker.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
        });

        // Выбор категории
        binding.cardCategory.tvTitle.setText("Выбор категории");
        View.OnClickListener openCategory = v -> openCategorySelection();
        binding.cardCategory.getRoot().setOnClickListener(openCategory);
        binding.cardCategory.ivArrow.setOnClickListener(openCategory);

        getParentFragmentManager().setFragmentResultListener("themeRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            ArrayList<String> selectedThemes = result.getStringArrayList("selectedThemes");
            if (selectedThemes != null) {
                viewModel.setSelectedCategories(selectedThemes);
            }
        });

        viewModel.getSelectedCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                binding.cardCategory.chipGroup.setVisibility(View.VISIBLE);
                binding.cardCategory.chipGroup.removeAllViews();

                for (String theme : categories) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_category, binding.cardCategory.chipGroup, false);
                    chip.setText(theme);
                    binding.cardCategory.chipGroup.addView(chip);
                }
            }
        });

        // Название со счетчиком символов (100)
        viewModel.getTitle().observe(getViewLifecycleOwner(), text -> {
            if (text != null && !binding.cardTitle.etTitle.getText().toString().equals(text)) {
                binding.cardTitle.etTitle.setText(text);
            }
        });

        binding.cardTitle.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.cardTitle.tvTitleCounter.setText(s.length() + "/100");
                viewModel.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Описание со счетчиком символов (400) и хештегами
        viewModel.getDescription().observe(getViewLifecycleOwner(), text -> {
            if (text != null && !binding.cardDescription.etDescription.getText().toString().equals(text)) {
                isUpdatingDescription = true;
                binding.cardDescription.etDescription.setText(text);
                isUpdatingDescription = false;
            }
        });

        viewModel.getHashtags().observe(getViewLifecycleOwner(), tags -> {
            if (tags != null) {
                hashtagSet.clear();
                hashtagSet.addAll(tags);
                refreshHashtagChips(binding.cardDescription.chipGroupTags);
            }
        });

        binding.cardDescription.tvAddHashtag.setOnClickListener(v -> showAddHashtagDialog(binding.cardDescription.chipGroupTags));

        binding.cardDescription.etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingDescription) return;

                binding.cardDescription.tvDescCounter.setText(s.length() + "/400");
                viewModel.setDescription(s.toString());

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
                    binding.cardDescription.etDescription.setText(updatedText);
                    binding.cardDescription.etDescription.setSelection(updatedText.length());
                    viewModel.setDescription(updatedText);
                    isUpdatingDescription = false;

                    viewModel.setHashtags(new LinkedHashSet<>(hashtagSet));
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
            String imageUriStr = args.getMaterialImageUriString();
            targetPosition = args.getMaterialPosition();

            if (title != null && !title.isEmpty() && (viewModel.getTitle().getValue() == null || viewModel.getTitle().getValue().isEmpty())) {
                binding.cardTitle.etTitle.setText(title);
                viewModel.setTitle(title);
            }
            if (description != null && !description.isEmpty() && (viewModel.getDescription().getValue() == null || viewModel.getDescription().getValue().isEmpty())) {
                binding.cardDescription.etDescription.setText(description);
                viewModel.setDescription(description);
            }
            if (iconRes != 0) {
                currentIconRes = iconRes;
                if (selectedImageUri == null) {
                    binding.ivMaterialImage.setImageResource(iconRes);
                }
            }
            if (imageUriStr != null && !imageUriStr.isEmpty() && viewModel.getImageUri().getValue() == null) {
                selectedImageUri = Uri.parse(imageUriStr);
                viewModel.setImageUri(selectedImageUri);
            }
            if (tag != null && !tag.isEmpty() && (viewModel.getHashtags().getValue() == null || viewModel.getHashtags().getValue().isEmpty())) {
                String[] tagsArray = tag.split("#|\\s+");
                for (String t : tagsArray) {
                    String clean = t.replace("#", "").trim();
                    if (!clean.isEmpty()) {
                        hashtagSet.add(clean);
                    }
                }
                viewModel.setHashtags(new LinkedHashSet<>(hashtagSet));
            }
        }

        final int position = targetPosition;
        binding.btnSave.setOnClickListener(v -> {
            String updatedTitle = binding.cardTitle.etTitle.getText().toString().trim();
            String updatedDesc = binding.cardDescription.etDescription.getText().toString().trim();

            String updatedTag = null;
            if (!hashtagSet.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String t : hashtagSet) {
                    if (sb.length() > 0) sb.append(" #");
                    sb.append(t);
                }
                updatedTag = sb.toString();
            }

            String imageUriString = (selectedImageUri != null) ? selectedImageUri.toString() : null;

            Bundle result = new Bundle();
            result.putInt("position", position);
            result.putString("title", updatedTitle);
            result.putString("description", updatedDesc);
            result.putString("tag", updatedTag);
            result.putInt("iconRes", currentIconRes);
            result.putString("imageUriString", imageUriString);

            getParentFragmentManager().setFragmentResult("editMaterialSaveRequest", result);

            NavController navController = NavHostFragment.findNavController(EditMaterialFragment.this);
            if (!navController.popBackStack(R.id.storageFragmentMain, false)) {
                NavDirections action =
                        EditMaterialFragmentDirections.actionEditMaterialFragmentToStorageFragmentMain();
                navController.navigate(action);
            }
        });
    }

    private void openCategorySelection() {
        ArrayList<String> currentThemes = new ArrayList<>();
        for (int i = 0; i < binding.cardCategory.chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.cardCategory.chipGroup.getChildAt(i);
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
            if (viewModel != null) {
                viewModel.setHashtags(new LinkedHashSet<>(hashtagSet));
            } else {
                refreshHashtagChips(chipGroup);
            }
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
                if (viewModel != null) {
                    viewModel.setHashtags(new LinkedHashSet<>(hashtagSet));
                } else {
                    refreshHashtagChips(chipGroup);
                }
            });
            chipGroup.addView(tagView);
        }
    }

    private void showAddHashtagDialog(ChipGroup chipGroupTags) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogAddHashtagBinding dialogBinding = DialogAddHashtagBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();

        String[] popularTags = {"вязание", "ремонт", "игрушки", "пряжа", "шитье", "реставрация", "поделки", "новогодние"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.item_dropdown_tag, popularTags);
        dialogBinding.etNewTag.setAdapter(adapter);

        dialogBinding.etNewTag.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTag = (String) parent.getItemAtPosition(position);
            dialogBinding.etNewTag.setText(selectedTag);
            dialogBinding.etNewTag.setSelection(selectedTag.length());
        });

        dialogBinding.btnAddTag.setOnClickListener(v -> {
            String newTag = dialogBinding.etNewTag.getText().toString().trim();
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

    private void setupToolbarForEditScreen() {
        binding.customToolbar.tvToolbarTitle.setText("Редактировать материал");

        binding.customToolbar.btnLeftAction.setImageResource(R.drawable.ic_arrow_back);
        binding.customToolbar.btnLeftAction.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        binding.customToolbar.btnRightAction.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
