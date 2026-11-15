package com.example.diyproject.ui.instruction;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;
import com.example.diyproject.databinding.DialogAddHashtagBinding;
import com.example.diyproject.databinding.FragmentEditInstructionBinding;
import com.example.diyproject.ui.common.SaveDraftBottomSheet;
import com.example.diyproject.ui.storage.MaterialAdapter;
import com.example.diyproject.ui.storage.MaterialItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditInstructionFragment extends Fragment {

    private final Set<String> hashtagSet = new LinkedHashSet<>();
    private boolean isUpdatingDescription = false;

    private ArrayList<String> selectedMaterialTitles = new ArrayList<>();
    private List<MaterialItem> selectedMaterialItems = new ArrayList<>();
    private List<InstructionStep> steps = new ArrayList<>();

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private InstructionStepAdapter stepAdapter;
    private int pendingImagePosition = -1;

    private EditInstructionViewModel viewModel;
    private FragmentEditInstructionBinding binding;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickStepImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null && stepAdapter != null && pendingImagePosition != -1) {
                    stepAdapter.setImageForStep(pendingImagePosition, uri);
                    pendingImagePosition = -1;
                    if (viewModel != null) {
                        viewModel.setInstructionSteps(steps);
                    }
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null && binding != null) {
                        loadPreview(uri, binding.ivInstructionImage);
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
        binding = FragmentEditInstructionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EditInstructionViewModel.class);

        // Аргументы с картинкой при старте
        if (getArguments() != null) {
            String imageUriString = getArguments().getString("instructionImageUri");
            if (imageUriString != null && !imageUriString.isEmpty() && !"empty".equals(imageUriString)) {
                Uri imageUri = Uri.parse(imageUriString);
                loadPreview(imageUri, binding.ivInstructionImage);
                viewModel.setImageUri(imageUri);
            }
        }

        viewModel.getImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                loadPreview(uri, binding.ivInstructionImage);
            }
        });

        binding.frameImagePicker.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
        });

        binding.customToolbar.btnLeftAction.setOnClickListener(v -> {
            SaveDraftBottomSheet bottomSheet = new SaveDraftBottomSheet();
            bottomSheet.setListener(new SaveDraftBottomSheet.OnDraftActionListener() {
                @Override
                public void onDelete() {
                    NavHostFragment.findNavController(EditInstructionFragment.this).popBackStack();
                }

                @Override
                public void onSaveDraft() {
                    NavHostFragment.findNavController(EditInstructionFragment.this).popBackStack();
                }
            });
            bottomSheet.show(getParentFragmentManager(), "SaveDraftBottomSheet");
        });

        // "Выбор категории" вверху
        binding.cardTheme.tvTitle.setText("Выбор категории");
        View.OnClickListener openCategory = v -> openCategorySelection();
        binding.cardTheme.getRoot().setOnClickListener(openCategory);
        binding.cardTheme.ivArrow.setOnClickListener(openCategory);

        viewModel.getSelectedCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                binding.cardTheme.chipGroup.setVisibility(View.VISIBLE);
                binding.cardTheme.chipGroup.removeAllViews();

                for (String theme : categories) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_category, binding.cardTheme.chipGroup, false);
                    chip.setText(theme);
                    binding.cardTheme.chipGroup.addView(chip);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("themeRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            ArrayList<String> selectedThemes = result.getStringArrayList("selectedThemes");
            if (selectedThemes != null) {
                viewModel.setSelectedCategories(selectedThemes);
            }
        });

        // Карточка времени
        viewModel.getSelectedTime().observe(getViewLifecycleOwner(), time -> {
            if (time != null && !time.isEmpty()) {
                binding.cardTime.tvSelectedTime.setText(time);
            }
        });

        binding.cardTime.cardTimeRoot.setOnClickListener(v -> {
            ContextThemeWrapper wrapper = new ContextThemeWrapper(requireContext(), R.style.PopupMenuTheme);
            PopupMenu popupMenu = new PopupMenu(wrapper, v, Gravity.CENTER);
            popupMenu.getMenu().add("15 минут");
            popupMenu.getMenu().add("30 минут");
            popupMenu.getMenu().add("1 час");
            popupMenu.getMenu().add("2 часа");
            popupMenu.getMenu().add("3 часа");
            popupMenu.getMenu().add("Более 3 часов");

            popupMenu.setOnMenuItemClickListener(item -> {
                String chosenTime = item.getTitle().toString();
                viewModel.setSelectedTime(chosenTime);
                return true;
            });
            popupMenu.show();
        });

        // Редактор названия (100 символов)
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

        // Описание (400 символов) и хештеги
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

                String currentText = s.toString();
                viewModel.setDescription(currentText);

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

                String finalText;

                if (found) {
                    matcher.appendTail(sb);
                    finalText = sb.toString();

                    isUpdatingDescription = true;
                    binding.cardDescription.etDescription.setText(finalText);
                    binding.cardDescription.etDescription.setSelection(finalText.length());
                    viewModel.setDescription(finalText);
                    isUpdatingDescription = false;

                    viewModel.setHashtags(new LinkedHashSet<>(hashtagSet));
                } else {
                    finalText = currentText;
                }

                binding.cardDescription.tvDescCounter.setText(finalText.length() + "/400");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // "Выбор материала" внизу
        binding.cardSelectMaterial.tvTitle.setText("Выбор материала");
        View.OnClickListener openMaterialSelection = v -> {
            EditInstructionFragmentDirections.ActionEditInstructionFragmentToMaterialSelectionFragment action =
                    EditInstructionFragmentDirections.actionEditInstructionFragmentToMaterialSelectionFragment();

            if (selectedMaterialTitles.isEmpty()) {
                action.setSelectedMaterials(null);
            } else {
                action.setSelectedMaterials(selectedMaterialTitles.toArray(new String[0]));
            }
            NavHostFragment.findNavController(EditInstructionFragment.this).navigate(action);
        };

        binding.cardSelectMaterial.getRoot().setOnClickListener(openMaterialSelection);
        binding.cardSelectMaterial.ivArrow.setOnClickListener(openMaterialSelection);

        viewModel.getSelectedMaterialItems().observe(getViewLifecycleOwner(), materials -> {
            if (materials != null) {
                selectedMaterialItems = materials;
                if (materials.isEmpty()) {
                    binding.rvSelectedMaterials.setVisibility(View.GONE);
                } else {
                    binding.rvSelectedMaterials.setVisibility(View.VISIBLE);
                    setupSelectedMaterialsAdapter(binding.rvSelectedMaterials);
                }
            }
        });

        viewModel.getSelectedMaterialTitles().observe(getViewLifecycleOwner(), titles -> {
            if (titles != null) {
                selectedMaterialTitles = titles;
            }
        });

        // Результат из экрана выбора материалов
        getParentFragmentManager().setFragmentResultListener("materialRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            ArrayList<String> selectedTitles = result.getStringArrayList("selectedMaterials");
            @SuppressWarnings("unchecked")
            ArrayList<MaterialItem> selectedList = (ArrayList<MaterialItem>) result.getSerializable("selectedMaterialsList");

            if (selectedTitles != null) {
                selectedMaterialTitles = selectedTitles;
                viewModel.setSelectedMaterialTitles(selectedTitles);

                List<MaterialItem> newItems = new ArrayList<>();
                for (String title : selectedTitles) {
                    MaterialItem existing = findMaterialByTitle(selectedMaterialItems, title);
                    if (existing != null) {
                        newItems.add(existing);
                    } else if (selectedList != null) {
                        MaterialItem fromSelection = findMaterialByTitle(selectedList, title);
                        if (fromSelection != null) {
                            newItems.add(fromSelection);
                        } else {
                            newItems.add(new MaterialItem(title, "", null, R.drawable.ic_material));
                        }
                    } else {
                        newItems.add(new MaterialItem(title, "", null, R.drawable.ic_material));
                    }
                }
                selectedMaterialItems = newItems;
                viewModel.setSelectedMaterialItems(newItems);
            }
        });

        // Результат после редактирования конкретного материала в EditMaterialFragment
        getParentFragmentManager().setFragmentResultListener("editMaterialSaveRequest", getViewLifecycleOwner(), (requestKey, result) -> {
            int position = result.getInt("position", -1);
            String title = result.getString("title");
            String description = result.getString("description");
            String tag = result.getString("tag");
            int iconRes = result.getInt("iconRes", 0);
            String imageUriString = result.getString("imageUriString");

            if (position >= 0 && position < selectedMaterialItems.size()) {
                MaterialItem oldItem = selectedMaterialItems.get(position);
                String oldTitle = oldItem.getTitle();

                String newTitle = (title != null && !title.isEmpty()) ? title : oldTitle;
                String newDesc = (description != null) ? description : oldItem.getDescription();
                String newTag = tag;
                int newIcon = (iconRes != 0) ? iconRes : oldItem.getIconRes();
                String newImageUri = (imageUriString != null) ? imageUriString : oldItem.getImageUriString();

                MaterialItem updatedItem = new MaterialItem(newTitle, newDesc, newTag, newIcon, newImageUri);
                selectedMaterialItems.set(position, updatedItem);

                if (!newTitle.equals(oldTitle)) {
                    int titleIdx = selectedMaterialTitles.indexOf(oldTitle);
                    if (titleIdx != -1) {
                        selectedMaterialTitles.set(titleIdx, newTitle);
                    }
                }

                viewModel.setSelectedMaterialItems(new ArrayList<>(selectedMaterialItems));
                viewModel.setSelectedMaterialTitles(new ArrayList<>(selectedMaterialTitles));
            }
        });

        // Настройка RecyclerView для шагов
        binding.rvInstructionSteps.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getInstructionSteps().observe(getViewLifecycleOwner(), stepList -> {
            if (stepList != null) {
                steps = stepList;
                if (steps.isEmpty()) {
                    steps.add(new InstructionStep("", null));
                }
                setupStepsAdapter();
            }
        });

        if (viewModel.getInstructionSteps().getValue() == null || viewModel.getInstructionSteps().getValue().isEmpty()) {
            steps = new ArrayList<>();
            steps.add(new InstructionStep("", null));
            viewModel.setInstructionSteps(steps);
        }

        binding.tvAddStep.setOnClickListener(v -> {
            InstructionStep newStep = new InstructionStep("", null);
            steps.add(newStep);
            viewModel.setInstructionSteps(steps);
            binding.rvInstructionSteps.smoothScrollToPosition(steps.size() - 1);
        });
    }

    private void setupStepsAdapter() {
        stepAdapter = new InstructionStepAdapter(steps, position -> {
            pendingImagePosition = position;
            pickStepImage.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        binding.rvInstructionSteps.setAdapter(stepAdapter);
    }

    private void setupSelectedMaterialsAdapter(RecyclerView rvSelectedMaterials) {
        MaterialAdapter adapter = new MaterialAdapter(selectedMaterialItems, true, false, new MaterialAdapter.OnMaterialClickListener() {
            @Override
            public void onCardClick(MaterialItem item) {}

            @Override
            public void onSettingsClick(MaterialItem item) {
                int index = selectedMaterialItems.indexOf(item);
                EditInstructionFragmentDirections.ActionEditInstructionFragmentToEditMaterialFragment action =
                        EditInstructionFragmentDirections.actionEditInstructionFragmentToEditMaterialFragment();
                action.setMaterialTitle(item.getTitle());
                action.setMaterialDescription(item.getDescription());
                action.setMaterialTag(item.getTag());
                action.setMaterialIconRes(item.getIconRes());
                action.setMaterialImageUriString(item.getImageUriString());
                action.setMaterialPosition(index);

                NavHostFragment.findNavController(EditInstructionFragment.this).navigate(action);
            }
        });

        rvSelectedMaterials.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSelectedMaterials.setAdapter(adapter);
    }

    private MaterialItem findMaterialByTitle(List<MaterialItem> list, String title) {
        for (MaterialItem item : list) {
            if (item.getTitle().equals(title)) {
                return item;
            }
        }
        return null;
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

    private void openCategorySelection() {
        ArrayList<String> currentThemes = new ArrayList<>();
        for (int i = 0; i < binding.cardTheme.chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.cardTheme.chipGroup.getChildAt(i);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
