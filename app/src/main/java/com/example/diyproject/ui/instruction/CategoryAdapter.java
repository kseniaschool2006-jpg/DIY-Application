package com.example.diyproject.ui.instruction;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemCategoryChoiceBinding;

import java.util.ArrayList;
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
        ItemCategoryChoiceBinding binding;

        public ViewHolder(@NonNull ItemCategoryChoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryChoiceBinding binding = ItemCategoryChoiceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.binding.tvCategoryName.setText(category);
        holder.binding.checkBox.setChecked(selectedPositions.contains(position));

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

    public List<String> getSelectedCategories() {
        List<String> selected = new ArrayList<>();
        for (Integer pos : selectedPositions) {
            selected.add(categories.get(pos));
        }
        return selected;
    }
}
