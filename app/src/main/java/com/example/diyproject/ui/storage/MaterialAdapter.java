package com.example.diyproject.ui.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {


    public interface OnMaterialClickListener {
        void onCardClick(MaterialItem item);
        void onSettingsClick(MaterialItem item);
    }
    private final List<MaterialItem> items;
    private final boolean showSettings;

    private final OnMaterialClickListener clickListener;

    public MaterialAdapter(List<MaterialItem> items, boolean showSettings, OnMaterialClickListener clickListener) {
        this.items = items;
        this.showSettings = showSettings;
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvTag;
        ImageView ivSettings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivMaterialImage);
            tvTitle = itemView.findViewById(R.id.tvMaterialTitle);
            tvDescription = itemView.findViewById(R.id.tvMaterialSubtitle);
            tvTag = itemView.findViewById(R.id.tvMaterialTag);
            ivSettings = itemView.findViewById(R.id.btnSettings);
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


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}