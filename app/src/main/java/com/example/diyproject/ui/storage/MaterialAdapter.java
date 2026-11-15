package com.example.diyproject.ui.storage;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;
import com.example.diyproject.databinding.ItemMaterialCardBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    public interface OnMaterialClickListener {
        void onCardClick(MaterialItem item);
        void onSettingsClick(MaterialItem item);
    }

    private final List<MaterialItem> items;
    private final boolean showSettings;
    private final boolean showCheckBox;
    private final Set<Integer> selectedPositions = new HashSet<>();
    private final OnMaterialClickListener clickListener;

    public MaterialAdapter(List<MaterialItem> items, boolean showSettings, boolean showCheckBox, OnMaterialClickListener clickListener) {
        this(items, showSettings, showCheckBox, null, clickListener);
    }

    public MaterialAdapter(List<MaterialItem> items, boolean showSettings, boolean showCheckBox, List<String> preSelectedTitles, OnMaterialClickListener clickListener) {
        this.items = items;
        this.showSettings = showSettings;
        this.showCheckBox = showCheckBox;
        this.clickListener = clickListener;

        if (preSelectedTitles != null && !preSelectedTitles.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                if (preSelectedTitles.contains(items.get(i).getTitle())) {
                    selectedPositions.add(i);
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMaterialCardBinding binding;

        public ViewHolder(@NonNull ItemMaterialCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMaterialCardBinding binding = ItemMaterialCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaterialItem item = items.get(position);

        if (item.getImageUriString() != null && !item.getImageUriString().isEmpty()) {
            holder.binding.ivMaterialImage.setImageURI(Uri.parse(item.getImageUriString()));
        } else {
            holder.binding.ivMaterialImage.setImageResource(item.getIconRes());
        }

        holder.binding.tvMaterialTitle.setText(item.getTitle());
        holder.binding.tvMaterialSubtitle.setText(item.getDescription());

        List<String> hashtags = parseHashtags(item.getTag());
        if (!hashtags.isEmpty()) {
            holder.binding.chipGroupMaterialTags.setVisibility(View.VISIBLE);
            holder.binding.chipGroupMaterialTags.removeAllViews();

            for (String tag : hashtags) {
                TextView tagView = (TextView) LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.item_hashtag, holder.binding.chipGroupMaterialTags, false);
                tagView.setText("# " + tag);
                holder.binding.chipGroupMaterialTags.addView(tagView);
            }
        } else {
            holder.binding.chipGroupMaterialTags.setVisibility(View.GONE);
        }

        if (showSettings) {
            holder.binding.btnSettings.setVisibility(View.VISIBLE);
            holder.binding.btnSettings.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onSettingsClick(item);
                }
            });
        } else {
            holder.binding.btnSettings.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onCardClick(item);
                }
            });
        }

        if (showCheckBox) {
            holder.binding.checkBox.setVisibility(View.VISIBLE);
            holder.binding.checkBox.setChecked(selectedPositions.contains(position));
            holder.itemView.setOnClickListener(v -> {
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove(position);
                } else {
                    selectedPositions.add(position);
                }
                notifyItemChanged(position);
            });
        } else {
            holder.binding.checkBox.setVisibility(View.GONE);
        }
    }

    private List<String> parseHashtags(String tagString) {
        List<String> tags = new ArrayList<>();
        if (tagString == null || tagString.trim().isEmpty()) return tags;

        String[] parts = tagString.split("#");
        for (String p : parts) {
            String clean = p.trim();
            if (!clean.isEmpty() && !tags.contains(clean)) {
                tags.add(clean);
            }
        }
        return tags;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<MaterialItem> getSelectedItems() {
        List<MaterialItem> selected = new ArrayList<>();
        for (Integer pos : selectedPositions) {
            selected.add(items.get(pos));
        }
        return selected;
    }
}
