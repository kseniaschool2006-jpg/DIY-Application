package com.example.diyproject.ui.instruction;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemInstructionStepBinding;

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
        ItemInstructionStepBinding binding;
        TextWatcher titleWatcher;
        TextWatcher subtitleWatcher;

        public ViewHolder(@NonNull ItemInstructionStepBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInstructionStepBinding binding = ItemInstructionStepBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstructionStep step = steps.get(position);

        if (holder.titleWatcher != null) {
            holder.binding.etStepTitle.removeTextChangedListener(holder.titleWatcher);
        }
        if (holder.subtitleWatcher != null) {
            holder.binding.etStepSubtitle.removeTextChangedListener(holder.subtitleWatcher);
        }

        holder.binding.etStepTitle.setText(step.getTitle());
        holder.binding.etStepSubtitle.setText(step.getSubtitle());
        holder.binding.etStepSubtitle.setHint("Шаг " + (position + 1));

        if (step.getImageUri() != null) {
            holder.binding.ivStepPreview.setImageURI(step.getImageUri());
            holder.binding.cardStepPreview.setVisibility(View.VISIBLE);
            holder.binding.ivStepIcon.setVisibility(View.GONE);
        } else {
            holder.binding.cardStepPreview.setVisibility(View.GONE);
            holder.binding.ivStepIcon.setVisibility(View.VISIBLE);
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

        holder.binding.etStepTitle.addTextChangedListener(holder.titleWatcher);
        holder.binding.etStepSubtitle.addTextChangedListener(holder.subtitleWatcher);

        View.OnClickListener pickImageListener = v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                imagePickListener.onImagePick(pos);
            }
        };
        holder.binding.ivStepIcon.setOnClickListener(pickImageListener);
        holder.binding.cardStepPreview.setOnClickListener(pickImageListener);
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
