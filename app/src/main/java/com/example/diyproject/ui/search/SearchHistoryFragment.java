package com.example.diyproject.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
                openResult(item.getTitle());
            }

            @Override
            public void onRemoveClick(SearchHistoryItem item) {
                // TODO: с БД здесь же удалять запись из базы
                adapter.removeItem(item);
            }
        });
        rv.setAdapter(adapter);
        adapter.setItems(getMockHistory());

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            hideKeyboard();
            NavHostFragment.findNavController(this).popBackStack();
        });

        Bundle args = getArguments();
        if (args != null) {
            String q = args.getString("query", "");
            etSearch.setText(q);
            etSearch.setSelection(q.length());
        }

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String text = etSearch.getText().toString().trim();
                if (!text.isEmpty()) {
                    openResult(text);
                }
                return true;
            }
            return false;
        });

        etSearch.requestFocus();
        etSearch.post(() -> {
            InputMethodManager imm = (InputMethodManager)
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    private void openResult(String text) {
        hideKeyboard();
        Bundle out = new Bundle();
        out.putString("query", text);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_searchHistory_to_searchResult, out);
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