package com.example.diyproject.ui.instruction;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;

import java.util.List;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepAdapter.ViewHolder> {

    public interface OnImagePickListener {
        void onImagePick(int position);
    }

    private final List<InstructionStep> steps;
    private final OnImagePickListener imagePickListener;

    public InstructionStepAdapter(List<InstructionStep> steps, OnImagePickListener listener) {
        this.steps = steps;
        this.imagePickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText etTitle;
        EditText etSubtitle;
        ImageButton btnGallery;
        View cardPreview;
        ImageView ivPreview;
        TextWatcher titleWatcher;
        TextWatcher subtitleWatcher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etTitle = itemView.findViewById(R.id.etStepTitle);
            etSubtitle = itemView.findViewById(R.id.etStepSubtitle);
            btnGallery = itemView.findViewById(R.id.ivStepIcon);
            cardPreview = itemView.findViewById(R.id.cardStepPreview);
            ivPreview = itemView.findViewById(R.id.ivStepPreview);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instruction_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstructionStep step = steps.get(position);

        if (holder.titleWatcher != null) {
            holder.etTitle.removeTextChangedListener(holder.titleWatcher);
        }
        if (holder.subtitleWatcher != null) {
            holder.etSubtitle.removeTextChangedListener(holder.subtitleWatcher);
        }

        holder.etTitle.setText(step.getTitle());
        holder.etSubtitle.setText(step.getSubtitle());
        holder.etSubtitle.setHint("Шаг " + (position + 1));

        if (step.getImageUri() != null) {
            holder.ivPreview.setImageURI(step.getImageUri());
            holder.cardPreview.setVisibility(View.VISIBLE);
            holder.btnGallery.setVisibility(View.GONE);
        } else {
            holder.cardPreview.setVisibility(View.GONE);
            holder.btnGallery.setVisibility(View.VISIBLE);
        }

        holder.titleWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    steps.get(pos).setTitle(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        holder.subtitleWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    steps.get(pos).setSubtitle(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        holder.etTitle.addTextChangedListener(holder.titleWatcher);
        holder.etSubtitle.addTextChangedListener(holder.subtitleWatcher);

        View.OnClickListener pickImageListener = v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                imagePickListener.onImagePick(pos);
            }
        };
        holder.btnGallery.setOnClickListener(pickImageListener);
        holder.cardPreview.setOnClickListener(pickImageListener);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setImageForStep(int position, Uri uri) {
        if (position >= 0 && position < steps.size()) {
            steps.get(position).setImageUri(uri);
            notifyItemChanged(position);
        }
    }
}
