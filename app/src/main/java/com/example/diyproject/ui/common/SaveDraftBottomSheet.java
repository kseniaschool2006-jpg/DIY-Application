package com.example.diyproject.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diyproject.databinding.DialogSaveDraftBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SaveDraftBottomSheet extends BottomSheetDialogFragment {

    public interface OnDraftActionListener {
        void onDelete();
        void onSaveDraft();
    }

    private OnDraftActionListener listener;
    private DialogSaveDraftBinding binding;

    public void setListener(OnDraftActionListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogSaveDraftBinding.inflate(inflater, container, false);

        binding.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete();
            dismiss();
        });

        binding.btnSaveDraft.setOnClickListener(v -> {
            if (listener != null) listener.onSaveDraft();
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
