package com.example.diyproject.instructionDetail;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemStepBinding;
import com.example.diyproject.model.InstructionStepItem;
import com.example.diyproject.model.StepMediaItem;

import java.util.ArrayList;
import java.util.List;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepAdapter.ViewHolder>{

    private final List<InstructionStepItem> steps = new ArrayList<>();
    private final StepMediaAdapter.OnMediaClickListener mediaClickListener;

    public InstructionStepAdapter(StepMediaAdapter.OnMediaClickListener mediaClickListener) {
        this.mediaClickListener = mediaClickListener;
    }

    public void setSteps(List<InstructionStepItem> newSteps) {
        steps.clear();
        if (newSteps != null) {
            steps.addAll(newSteps);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemStepBinding binding = ItemStepBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemStepBinding binding;
        private final StepMediaAdapter mediaAdapter;

        ViewHolder(ItemStepBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.mediaAdapter = new StepMediaAdapter(mediaClickListener);

            binding.rvStepPhotos.setLayoutManager(
                    new LinearLayoutManager(binding.getRoot().getContext())
            );
            binding.rvStepPhotos.setAdapter(mediaAdapter);
        }

        void bind(InstructionStepItem step) {
            binding.tvStepTitle.setText(step.getStepNumber() + ". " + step.getTitle());

            if (TextUtils.isEmpty(step.getDescription())) {
                binding.tvStepDescription.setVisibility(View.GONE);
            } else {
                binding.tvStepDescription.setVisibility(View.VISIBLE);
                binding.tvStepDescription.setText(step.getDescription());
            }

            List<StepMediaItem> media = step.getMediaList();
            if (media == null || media.isEmpty()) {
                binding.rvStepPhotos.setVisibility(View.GONE);
            } else {
                binding.rvStepPhotos.setVisibility(View.VISIBLE);
                mediaAdapter.setItems(media);
            }
        }
    }
}
