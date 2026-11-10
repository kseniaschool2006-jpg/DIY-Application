package com.example.diyproject;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        //  ОТСЮДА НАЧИНАЕТСЯ НИЖНЕЕ МЕНЮ
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController nav = host.getNavController();

        // ЗАМЕНИТЬ на реальные id фрагментов из nav_graph
        // -1 = экрана пока нет, иконка просто ничего не делает
        int[] destIds = {
                R.id.toCheckFragment,       // дом
                R.id.storageFragmentMain,   // поиск
                -1,                         // добавление
                -1,                         // архив
                -1                          // профиль
        };

        View[] items = {findViewById(R.id.navHome), findViewById(R.id.navSearch),
                findViewById(R.id.navAdd), findViewById(R.id.navArchive),
                findViewById(R.id.navProfile)};
        View bottomBar = findViewById(R.id.bottomBar);

        NavOptions options = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(nav.getGraph().getStartDestinationId(), false)
                .build();

        for (int i = 0; i < items.length; i++) {
            final int destId = destIds[i];
            if (destId == -1) continue;
            items[i].setOnClickListener(v -> {
                if (nav.getCurrentDestination() != null
                        && nav.getCurrentDestination().getId() != destId) {
                    nav.navigate(destId, null, options);
                }
            });
        }

        nav.addOnDestinationChangedListener((controller, destination, args) -> {
            boolean isTab = false;
            for (int i = 0; i < items.length; i++) {
                boolean selected = destIds[i] != -1 && destination.getId() == destIds[i];
                items[i].setSelected(selected);
                items[i].setScaleX(selected ? 62f / 52f : 1f);
                items[i].setScaleY(selected ? 62f / 52f : 1f);
                if (selected) isTab = true;
            }
            bottomBar.setVisibility(isTab ? View.VISIBLE : View.GONE);
        });
    }
}