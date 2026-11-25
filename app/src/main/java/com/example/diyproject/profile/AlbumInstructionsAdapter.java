package com.example.diyproject.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemAlbumInstructionBinding;
import com.example.diyproject.model.AlbumInstructionItem;

import java.util.List;

public class AlbumInstructionsAdapter extends RecyclerView.Adapter<AlbumInstructionsAdapter.ViewHolder>{

    private final List<AlbumInstructionItem> items;
    private final OnInstructionClickListener listener;

    public interface OnInstructionClickListener {
        void onInstructionClick(AlbumInstructionItem item);
    }

    public AlbumInstructionsAdapter(List<AlbumInstructionItem> items, OnInstructionClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAlbumInstructionBinding binding = ItemAlbumInstructionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        AlbumInstructionItem item = items.get(position);
        holder.binding.tvInstructionTitle.setText(item.title);
        holder.binding.ivInstructionImage.setImageResource(item.imageResId);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onInstructionClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAlbumInstructionBinding binding;

        ViewHolder( ItemAlbumInstructionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
