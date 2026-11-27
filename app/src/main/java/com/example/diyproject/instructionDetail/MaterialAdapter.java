package com.example.diyproject.instructionDetail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.databinding.ItemMaterialBinding;
import com.example.diyproject.model.MaterialItem;

import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder>{

    public interface OnMaterialCheckedChangeListener {
        void onMaterialCheckedChanged(MaterialItem item, boolean isChecked, int position);
    }

    private final List<MaterialItem> items = new ArrayList<>();
    private final OnMaterialCheckedChangeListener listener;

    public MaterialAdapter(OnMaterialCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setItems(List<MaterialItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMaterialBinding binding = ItemMaterialBinding.inflate(
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
        private final ItemMaterialBinding binding;

        ViewHolder(ItemMaterialBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MaterialItem item, int position) {
            binding.tvMaterialName.setText(item.getName());

            binding.cbMaterialAvailable.setOnCheckedChangeListener(null);
            binding.cbMaterialAvailable.setChecked(item.isInStock());

            binding.cbMaterialAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setInStock(isChecked);
                if (listener != null) {
                    listener.onMaterialCheckedChanged(item, isChecked, position);
                }
            });
        }
    }

}
