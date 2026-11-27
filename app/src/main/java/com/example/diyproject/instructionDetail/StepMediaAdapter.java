package com.example.diyproject.instructionDetail;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diyproject.R;
import com.example.diyproject.databinding.ItemStepPhotoBinding;
import com.example.diyproject.model.StepMediaItem;

import java.util.ArrayList;
import java.util.List;

public class StepMediaAdapter extends RecyclerView.Adapter<StepMediaAdapter.ViewHolder>{

    public interface OnMediaClickListener {
        void onMediaClick(StepMediaItem item, int position);
    }

    private final List<StepMediaItem> items = new ArrayList<>();
    private final OnMediaClickListener listener;

    public StepMediaAdapter(OnMediaClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<StepMediaItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemStepPhotoBinding binding = ItemStepPhotoBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemStepPhotoBinding binding;

        ViewHolder(ItemStepPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(StepMediaItem item, int position) {
            Glide.with(binding.ivStepMedia.getContext())
                    .load(Uri.parse(item.getUri()))
                    .placeholder(R.drawable.post_image)
                    .error(R.drawable.post_image)
                    .centerCrop()
                    .into(binding.ivStepMedia);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMediaClick(item, position);
                }
            });
        }
    }
}
