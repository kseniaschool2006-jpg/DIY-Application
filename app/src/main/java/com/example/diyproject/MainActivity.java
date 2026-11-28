package com.example.diyproject;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diyproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController nav = host.getNavController();

        // Регистрируем лаунчер выбора медиафайла для создания инструкции через меню navAdd
        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        if (isVideoDurationValid(uri)) {
                            Bundle args = new Bundle();
                            args.putString("instructionImageUri", uri.toString());

                            NavOptions options = new NavOptions.Builder()
                                    .setLaunchSingleTop(true)
                                    .build();

                            nav.navigate(R.id.editInstructionFragment, args, options);
                        } else {
                            Toast.makeText(this, "Видео должно быть не длиннее 30 секунд", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // ID главных вкладок, соответствующих иконкам нижнего меню
        int[] destIds = {
                R.id.homeFragment,
                R.id.searchStartFragment,
                -1, // navAdd вызывает галерею и ведет на редактирование инструкции
                R.id.storageFragmentMain,
                R.id.profileFragment
        };

        // Список ТОЛЬКО главных экранов, на которых должно быть видно нижнее меню
        int[] mainVisibleScreens = {
                R.id.homeFragment,
                R.id.searchStartFragment,
                R.id.storageFragmentMain,
                R.id.profileFragment,
                R.id.searchResultFragment
        };

        View[] items = {
                findViewById(R.id.navHome),
                findViewById(R.id.navSearch),
                findViewById(R.id.navAdd),
                findViewById(R.id.navArchive),
                findViewById(R.id.navProfile)
        };

        View bottomBar = findViewById(R.id.bottomBar);

        NavOptions options = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(nav.getGraph().getStartDestinationId(), false)
                .build();

        for (int i = 0; i < items.length; i++) {
            final int destId = destIds[i];

            if (items[i] != null && items[i].getId() == R.id.navAdd) {
                items[i].setOnClickListener(v -> {
                    pickMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                            .build());
                });
            } else if (items[i] != null && destId != -1) {
                items[i].setOnClickListener(v -> {
                    if (nav.getCurrentDestination() != null
                            && nav.getCurrentDestination().getId() != destId) {
                        nav.navigate(destId, null, options);
                    }
                });
            }
        }

        // Автоматическое отображение/скрытие нижнего меню при переходе по экранам
        nav.addOnDestinationChangedListener((controller, destination, args) -> {
            int currentId = destination.getId();

            for (int i = 0; i < items.length; i++) {
                boolean selected = destIds[i] != -1 && currentId == destIds[i];
                if (items[i] != null) {
                    items[i].setSelected(selected);
                    items[i].setScaleX(selected ? 62f / 52f : 1f);
                    items[i].setScaleY(selected ? 62f / 52f : 1f);
                }
            }

            boolean showBar = false;
            for (int id : mainVisibleScreens) {
                if (currentId == id) {
                    showBar = true;
                    break;
                }
            }

            if (bottomBar != null) {
                bottomBar.setVisibility(showBar ? View.VISIBLE : View.GONE);
            }
        });
    }

    private boolean isVideoDurationValid(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        if (mimeType == null || !mimeType.startsWith("video/")) {
            return true;
        }

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, uri);
            String durationStr = retriever.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();

            if (durationStr != null) {
                long durationMs = Long.parseLong(durationStr);
                return durationMs <= 30000;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
