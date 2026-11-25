package com.example.diyproject.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemUserCreatedPhotoBinding;
import com.example.diyproject.model.PostItemProfile;

import java.util.List;

public class UserCreatedAdapter extends RecyclerView.Adapter<UserCreatedAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(PostItemProfile item);
    }

    private final List<PostItemProfile> items;
    private final OnItemClickListener listener;

    public UserCreatedAdapter(List<PostItemProfile> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserCreatedPhotoBinding binding = ItemUserCreatedPhotoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        PostItemProfile item = items.get(position);
        holder.binding.ivItemPhoto.setImageResource(item.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserCreatedPhotoBinding binding;

        public ViewHolder( ItemUserCreatedPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
