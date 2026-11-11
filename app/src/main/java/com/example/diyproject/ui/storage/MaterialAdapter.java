package com.example.diyproject.ui.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;

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
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvTag;
        ImageView ivSettings;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivMaterialImage);
            tvTitle = itemView.findViewById(R.id.tvMaterialTitle);
            tvDescription = itemView.findViewById(R.id.tvMaterialSubtitle);
            tvTag = itemView.findViewById(R.id.tvMaterialTag);
            ivSettings = itemView.findViewById(R.id.btnSettings);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaterialItem item = items.get(position);

        holder.ivIcon.setImageResource(item.getIconRes());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());

        if (item.getTag() != null && !item.getTag().isEmpty()) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText("# " + item.getTag());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (showSettings) {
            holder.ivSettings.setVisibility(View.VISIBLE);
            holder.ivSettings.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onSettingsClick(item);
                }
            });
        } else {
            holder.ivSettings.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onCardClick(item);
                }
            });
        }

        if (showCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(selectedPositions.contains(position));
            holder.itemView.setOnClickListener(v -> {
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove(position);
                } else {
                    selectedPositions.add(position);
                }
                notifyItemChanged(position);
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
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
