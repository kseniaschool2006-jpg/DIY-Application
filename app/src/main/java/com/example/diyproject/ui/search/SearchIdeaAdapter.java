package com.example.diyproject.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;

import java.util.ArrayList;
import java.util.List;

public class SearchIdeaAdapter extends RecyclerView.Adapter<SearchIdeaAdapter.ViewHolder> {

    public interface OnIdeaClickListener {
        void onIdeaClick(SearchIdea idea);
    }

    private final List<SearchIdea> items = new ArrayList<>();
    private final OnIdeaClickListener listener;

    public SearchIdeaAdapter(OnIdeaClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<SearchIdea> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_idea, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchIdea item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.image.setImageResource(item.getImageRes());
        holder.itemView.setOnClickListener(v -> listener.onIdeaClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_idea);
            title = itemView.findViewById(R.id.tv_idea_title);
        }
    }
}