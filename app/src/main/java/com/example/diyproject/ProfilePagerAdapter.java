package com.example.diyproject;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter( Fragment fragment) {
        super(fragment);
    }


    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new CreatedPostProfileFragment(); // Первая вкладка: Созданные
        } else {
            return new SavedPostProfileFragment();       // Вторая вкладка: Сохраненные
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Всего 2 вкладки
    }
}
