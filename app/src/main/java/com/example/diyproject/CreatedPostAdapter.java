package com.example.diyproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemCreatedPostBinding;
import com.example.diyproject.model.PostItemProfile;

import java.util.List;

public class CreatedPostAdapter extends RecyclerView.Adapter<CreatedPostAdapter.ViewHolder>{

    private final List<PostItemProfile> items;
    private final OnEditClickListener listener;

    public interface OnEditClickListener {
        void onEditClick(PostItemProfile item);
    }

    public CreatedPostAdapter(List<PostItemProfile> items, OnEditClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemCreatedPostBinding binding = ItemCreatedPostBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        PostItemProfile item = items.get(position);

        holder.binding.ivPostImage.setImageResource(item.imageResId);

        holder.binding.btnEditPost.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCreatedPostBinding binding;

        ViewHolder( ItemCreatedPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
