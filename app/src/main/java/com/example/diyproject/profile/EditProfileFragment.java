package com.example.diyproject.profile;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentEditProfileBinding;

import java.io.File;


public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private Uri cameraImageUri;
    private Uri selectedAvatarUri;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedAvatarUri = uri;
                    applyAvatarUri(uri);
                }
            });


    private final ActivityResultLauncher<Uri> takePhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (Boolean.TRUE.equals(success) && cameraImageUri != null) {
                    selectedAvatarUri = cameraImageUri;
                    applyAvatarUri(cameraImageUri);
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupKeyboardInsets();

        binding.topbarClose.btnClose.setOnClickListener(v ->
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
                );

        binding.topbarClose.btnSaveProfile.setEnabled(false);
        binding.topbarClose.btnSaveProfile.setAlpha(0.5f);

        binding.topbarClose.btnSaveProfile.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String username = binding.etUsername.getText().toString().trim();
            String about = binding.etAbout.getText().toString().trim();
            String website = binding.etWebsite.getText().toString().trim();

            // TODO: Сохранить данные профиля и выбранный selectedAvatarUri в БД
            Toast.makeText(requireContext(), "Профиль обновлен", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp();
        });

        setupRequiredFieldsWatcher();
        setupAvatarPicker();
        setupScrollOnFocus();
    }

    private void setupKeyboardInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            int bottomPadding = Math.max(imeInsets.bottom, systemBars.bottom);
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    bottomPadding
            );
            return windowInsets;
        });
    }



    private void setupRequiredFieldsWatcher() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        binding.etName.addTextChangedListener(watcher);
        binding.etUsername.addTextChangedListener(watcher);
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        if (binding == null) {
            return;
        }
        String name = binding.etName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();

        boolean isReady = !name.isEmpty() && !username.isEmpty();
        binding.topbarClose.btnSaveProfile.setEnabled(isReady);
        binding.topbarClose.btnSaveProfile.setAlpha(isReady ? 1.0f : 0.5f);
    }

    private void setupAvatarPicker() {
        binding.btnEditAvatar.setOnClickListener(v -> showAvatarSourceDialog());
    }

    private void showAvatarSourceDialog() {
        String[] options = {"Сделать фото", "Выбрать из галереи"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Фото профиля")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else if (which == 1) {
                        openPhotoPicker();
                    }
                })
                .show();
    }

    private void openPhotoPicker() {
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build();
        pickMediaLauncher.launch(request);
    }
    private void openCamera() {
        try {
            File avatarsDir = new File(requireContext().getCacheDir(), "avatars");
            if (!avatarsDir.exists() && !avatarsDir.mkdirs()) {
                Toast.makeText(requireContext(), "Не удалось создать папку для фото", Toast.LENGTH_SHORT).show();
                return;
            }

            File photoFile = new File(avatarsDir, "avatar_" + System.currentTimeMillis() + ".jpg");
            cameraImageUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    photoFile
            );

            takePhotoLauncher.launch(cameraImageUri);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Ошибка при запуске камеры", Toast.LENGTH_SHORT).show();
        }
    }
    private void applyAvatarUri(Uri uri) {
        if (binding == null) {
            return;
        }
        binding.ivAvatar.setPadding(0, 0, 0, 0);
        binding.ivAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        binding.ivAvatar.setImageURI(uri);
    }

    private void setupScrollOnFocus() {
        View.OnFocusChangeListener scrollToFieldListener = (v, hasFocus) -> {
            if (hasFocus && binding != null) {
                binding.scrollView.post(() -> binding.scrollView.smoothScrollTo(0, v.getBottom()));
            }
        };

        binding.etName.setOnFocusChangeListener(scrollToFieldListener);
        binding.etUsername.setOnFocusChangeListener(scrollToFieldListener);
        binding.etAbout.setOnFocusChangeListener(scrollToFieldListener);
        binding.etWebsite.setOnFocusChangeListener(scrollToFieldListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}