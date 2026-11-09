package com.example.diyproject.ui.instruction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<String> categories;
    private final Set<Integer> selectedPositions = new HashSet<>();

    public CategoryAdapter(List<String> categories, List<String> preSelected) {
        this.categories = categories;

        if (preSelected != null) {
            for (String pre : preSelected) {
                int index = categories.indexOf(pre);
                if (index != -1) {
                    selectedPositions.add(index);
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.tvName.setText(category);

        holder.checkBox.setChecked(selectedPositions.contains(position));

        holder.itemView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position);
            } else {
                selectedPositions.add(position);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public java.util.List<String> getSelectedCategories() {
        java.util.List<String> selected = new java.util.ArrayList<>();
        for (Integer pos : selectedPositions) {
            selected.add(categories.get(pos));
        }
        return selected;
    }
}