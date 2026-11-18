package com.example.diyproject.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class InstructionHomeAdapter
        extends RecyclerView.Adapter<InstructionHomeAdapter.ViewHolder> {

    public interface OnInstructionClickListener {
        void onInstructionClick(InstructionHome instruction);
    }

    private final List<InstructionHome> items = new ArrayList<>();
    private final OnInstructionClickListener listener;

    public InstructionHomeAdapter(OnInstructionClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<InstructionHome> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instruction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstructionHome item = items.get(position);

        holder.title.setText(item.getTitle());
        holder.image.setImageResource(item.getImageRes());

        holder.itemView.setOnClickListener(v -> listener.onInstructionClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ShapeableImageView image;
        final TextView title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_instruction);
            title = itemView.findViewById(R.id.tv_instruction_title);
        }
    }
}