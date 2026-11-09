package com.example.diyproject.ui.common;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diyproject.R;


//КЛАСС ПРОСТО ДЛЯ ПРОВЕРКИ, КОГДА БУДЕТ ГЛАВНЫЙ ЭКРАН ВСЕ ПЕРЕНЕСТИ
public class ToCheckFragment extends Fragment {

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        if (isVideoDurationValid(uri)) {
                            ToCheckFragmentDirections.ActionToCheckFragmentToEditInstructionFragment action =
                                    ToCheckFragmentDirections.actionToCheckFragmentToEditInstructionFragment();
                            action.setInstructionImageUri(uri.toString());
                            NavHostFragment.findNavController(ToCheckFragment.this).navigate(action);
                        } else {
                            Toast.makeText(getContext(), "Видео должно быть не длиннее 30 секунд", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

        private boolean isVideoDurationValid(Uri uri) {

            String mimeType = requireContext().getContentResolver().getType(uri);
            if (mimeType == null || !mimeType.startsWith("video/")) {
                return true;
            }

            try {
                android.media.MediaMetadataRetriever retriever = new android.media.MediaMetadataRetriever();
                retriever.setDataSource(requireContext(), uri);
                String durationStr = retriever.extractMetadata(
                        android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_check, container, false);

        Button btnCreate = view.findViewById(R.id.btnGoToEditInstruction);
        btnCreate.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build());
        });

        Button btnGoToStorage = view.findViewById(R.id.btnGoToStorage);
        btnGoToStorage.setOnClickListener(v -> {
            NavHostFragment.findNavController(ToCheckFragment.this).navigate(R.id.action_toCheckFragment_to_storageFragmentMain);
        });


        return view;
    }
}