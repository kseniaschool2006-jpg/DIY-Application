package com.example.diyproject.ui.search;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.diyproject.R;

public class SearchStartFragment extends Fragment {

    private final ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    // позже поиск по фото
                    Toast.makeText(requireContext(), "Фото получено", Toast.LENGTH_SHORT).show();
                }
            });

    public SearchStartFragment() {
        super(R.layout.fragment_search_start);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.search_bar).setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_searchStart_to_searchHistory));
        RecyclerView rv = view.findViewById(R.id.rv_ideas);

        rv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        SearchIdeaAdapter adapter = new SearchIdeaAdapter(idea -> {
            Bundle args = new Bundle();
            args.putString("query", idea.getTitle());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_searchStart_to_searchResult, args);
        });
        rv.setAdapter(adapter);
        adapter.setItems(getMockIdeas());
        
    }
    private List<SearchIdea> getMockIdeas() {
        List<SearchIdea> list = new ArrayList<>();
        list.add(new SearchIdea("Посуда", R.drawable.instruction_1));
        list.add(new SearchIdea("Хранение", R.drawable.instruction_2));
        list.add(new SearchIdea("Брелок", R.drawable.instruction_3));
        list.add(new SearchIdea("Свечи", R.drawable.instruction_4));
        list.add(new SearchIdea("Лепка", R.drawable.instruction_1));
        list.add(new SearchIdea("Гербарий", R.drawable.instruction_2));
        list.add(new SearchIdea("Вязание", R.drawable.instruction_3));
        list.add(new SearchIdea("Статуэтки", R.drawable.instruction_4));
        return list;
    }
}