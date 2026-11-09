package com.example.diyproject.ui.common;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diyproject.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SaveDraftBottomSheet extends BottomSheetDialogFragment {

    public interface OnDraftActionListener {
        void onDelete();
        void onSaveDraft();
    }

    private OnDraftActionListener listener;

    public void setListener(OnDraftActionListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_save_draft, container, false);

        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnSaveDraft = view.findViewById(R.id.btnSaveDraft);

        btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete();
            dismiss();
        });

        btnSaveDraft.setOnClickListener(v -> {
            if (listener != null) listener.onSaveDraft();
            dismiss();
        });

        return view;
    }
}
