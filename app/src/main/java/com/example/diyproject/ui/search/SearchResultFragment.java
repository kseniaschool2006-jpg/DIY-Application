package com.example.diyproject.ui.search;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.diyproject.ui.home.InstructionHomeAdapter;

import com.example.diyproject.R;

public class SearchResultFragment extends Fragment {

    private String query = "";

    public SearchResultFragment() {
        super(R.layout.fragment_search_result);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            query = args.getString("query", "");
        }

        TextView tvQuery = view.findViewById(R.id.tv_query);
        tvQuery.setText(query);

        tvQuery.setOnClickListener(v -> {
            Bundle out = new Bundle();
            out.putString("query", query);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_searchResult_to_searchHistory, out);
        });

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        RecyclerView rv = view.findViewById(R.id.rv_results);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        InstructionHomeAdapter adapter = new InstructionHomeAdapter(instruction -> {
            // TODO: открытие экрана инструкции
        });
        rv.setAdapter(adapter);
        adapter.setItems(InstructionRepository.search(query));
    }
}