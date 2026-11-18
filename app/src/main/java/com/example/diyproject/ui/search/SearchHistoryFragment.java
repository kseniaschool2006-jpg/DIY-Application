package com.example.diyproject.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diyproject.R;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryFragment extends Fragment {

    private EditText etSearch;
    private SearchHistoryAdapter adapter;

    public SearchHistoryFragment() {
        super(R.layout.fragment_search_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.et_search);

        RecyclerView rv = view.findViewById(R.id.rv_history);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SearchHistoryAdapter(new SearchHistoryAdapter.Listener() {
            @Override
            public void onItemClick(SearchHistoryItem item) {
                // позже
            }

            @Override
            public void onRemoveClick(SearchHistoryItem item) {
                // позже
                adapter.removeItem(item);
            }
        });
        rv.setAdapter(adapter);
        adapter.setItems(getMockHistory());

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            hideKeyboard();
            NavHostFragment.findNavController(this).popBackStack();
        });

        etSearch.requestFocus();
        etSearch.post(() -> {
            InputMethodManager imm = (InputMethodManager)
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    private List<SearchHistoryItem> getMockHistory() {
        List<SearchHistoryItem> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            list.add(new SearchHistoryItem(i, "что-то", R.drawable.instruction_1));
        }
        return list;
    }
}