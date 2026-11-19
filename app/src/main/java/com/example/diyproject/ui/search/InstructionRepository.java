package com.example.diyproject.ui.search;

import com.example.diyproject.R;
import com.example.diyproject.ui.home.InstructionHome;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InstructionRepository {

    public static List<InstructionHome> getAll() {
        List<InstructionHome> list = new ArrayList<>();
        list.add(new InstructionHome(1, "Брелок мышка своими руками быстро и легко",
                R.drawable.instruction_1, "брелок мышка бисер"));
        list.add(new InstructionHome(2, "Легкий брелок из бисера на булавке",
                R.drawable.instruction_2, "брелок бисер булавка"));
        list.add(new InstructionHome(3, "Мулине и руки, 10 часов вашего времени",
                R.drawable.instruction_3, "брелок мулине вязание"));
        list.add(new InstructionHome(4, "Брелок волна своими руками быстро и легко",
                R.drawable.instruction_4, "брелок бисер волна"));
        return list;
    }

    public static List<InstructionHome> search(String query) {
        String q = query.trim().toLowerCase(Locale.getDefault());
        List<InstructionHome> result = new ArrayList<>();
        for (InstructionHome item : getAll()) {
            String text = (item.getTitle() + " " + item.getTags())
                    .toLowerCase(Locale.getDefault());
            if (text.contains(q)) {
                result.add(item);
            }
        }
        return result;
    }
}