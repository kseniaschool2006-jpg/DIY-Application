package com.example.diyproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemAddAlbumBinding;
import com.example.diyproject.databinding.ItemAlbumBinding;
import com.example.diyproject.model.AlbumItemProfile;

import java.util.List;

public class SavedPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ALBUM = 0;
    private static final int TYPE_ADD = 1;

    private final List<AlbumItemProfile> items;
    private final OnAlbumClickListener listener;

    public interface OnAlbumClickListener {
        void onAlbumClick(AlbumItemProfile album);
        void onAddClick();
    }

    public SavedPostAdapter(List<AlbumItemProfile> items, OnAlbumClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isAddButton ? TYPE_ADD : TYPE_ALBUM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_ADD) {
            // Биндинг для кнопки "Добавить альбом"
            ItemAddAlbumBinding binding = ItemAddAlbumBinding.inflate(inflater, parent, false);
            return new AddViewHolder(binding);
        } else {
            // Биндинг для карточки альбома с коллажем
            ItemAlbumBinding binding = ItemAlbumBinding.inflate(inflater, parent, false);
            return new AlbumViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        AlbumItemProfile item = items.get(position);

        if (holder instanceof AlbumViewHolder) {
            AlbumViewHolder vh = (AlbumViewHolder) holder;

            vh.binding.tvAlbumTitle.setText(item.title);
            vh.binding.tvAlbumCount.setText(item.count + " инструкций");

            // Заполняем три картинки для коллажа альбома
            vh.binding.ivCover1.setImageResource(item.cover1);
            vh.binding.ivCover2.setImageResource(item.cover2);
            vh.binding.ivCover3.setImageResource(item.cover3);

            // Клик по самому альбому
            vh.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onAlbumClick(item);
            });

        } else if (holder instanceof AddViewHolder) {
            AddViewHolder vh = (AddViewHolder) holder;

            // Клик по карточке добавления альбома
            vh.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onAddClick();
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        private final ItemAlbumBinding binding;

        AlbumViewHolder(ItemAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class AddViewHolder extends RecyclerView.ViewHolder {
        private final ItemAddAlbumBinding binding;

        AddViewHolder( ItemAddAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
