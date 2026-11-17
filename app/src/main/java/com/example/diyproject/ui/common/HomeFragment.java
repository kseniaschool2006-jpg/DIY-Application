package com.example.diyproject.ui.common;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.diyproject.R;
import com.example.diyproject.ui.instruction.InstructionHome;
import com.example.diyproject.ui.instruction.InstructionHomeAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int TAB_FOR_YOU = 0;

    private InstructionHomeAdapter adapter;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rv_instructions);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new InstructionHomeAdapter(instruction -> {
            // следующий шаг открытие экрана инструкции
        });
        rv.setAdapter(adapter);

        TabLayout tabs = view.findViewById(R.id.tabLayout);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                loadInstructions(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        loadInstructions(TAB_FOR_YOU);
    }

    private void loadInstructions(int tab) {
        //  здесь будет запрос данных
        adapter.setItems(tab == TAB_FOR_YOU ? getMockInstructions() : new ArrayList<>());
    }

    private List<InstructionHome> getMockInstructions() {
        List<InstructionHome> list = new ArrayList<>();
       list.add(new InstructionHome(1, "Описание  инструкции", R.drawable.instruction_1));
        list.add(new InstructionHome(2, "Описание инструкции", R.drawable.instruction_2));
        list.add(new InstructionHome(3, "Описание инструкции", R.drawable.instruction_3));
        list.add(new InstructionHome(4, "Описание инструкции", R.drawable.instruction_4));
        list.add(new InstructionHome(1, "Описание  инструкции", R.drawable.instruction_1));
        list.add(new InstructionHome(2, "Описание инструкции", R.drawable.instruction_2));
        list.add(new InstructionHome(3, "Описание инструкции", R.drawable.instruction_3));
        list.add(new InstructionHome(4, "Описание инструкции", R.drawable.instruction_4));
        return list;
    }
}