package com.example.diyproject.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryAdapter
        extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    public interface Listener {
        void onItemClick(SearchHistoryItem item);
        void onRemoveClick(SearchHistoryItem item);
    }

    private final List<SearchHistoryItem> items = new ArrayList<>();
    private final Listener listener;

    public SearchHistoryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<SearchHistoryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void removeItem(SearchHistoryItem item) {
        int index = items.indexOf(item);
        if (index != -1) {
            items.remove(index);
            notifyItemRemoved(index);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchHistoryItem item = items.get(position);

        holder.title.setText(item.getTitle());
        holder.image.setImageResource(item.getImageRes());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.remove.setOnClickListener(v -> listener.onRemoveClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ShapeableImageView image;
        final TextView title;
        final ImageButton remove;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_history);
            title = itemView.findViewById(R.id.tv_history_title);
            remove = itemView.findViewById(R.id.btn_remove);
        }
    }
}