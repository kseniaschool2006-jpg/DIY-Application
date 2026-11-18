package com.example.diyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diyproject.databinding.FragmentProfileBinding;
import com.google.android.material.tabs.TabLayoutMediator;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadProfileHeaderMock();

        // 2. Настраиваем клики на кнопки в шапке
        setupHeaderListeners();

        // 3. Настраиваем вкладки "Созданные" и "Сохраненные"
        setupTabsAndViewPager();

        return view;
    }

    private void loadProfileHeaderMock() {
        // Обращаемся к элементам внутри <include android:id="@+id/profileHeader">
        binding.profileHeader.tvProfileName.setText("Мария");
        binding.profileHeader.tvProfileUsername.setText("@rukodelniza25");
        binding.profileHeader.tvProfileStats.setText("5 подписчиков • 1 подписка");
        binding.profileHeader.ivProfileAvatar.setImageResource(R.drawable.ic_launcher_background); // Замени на свою картинку аватара
    }

    private void setupHeaderListeners() {
        // Клик на кнопку "Редактирование профиля"
        binding.profileHeader.btnEditProfile.setOnClickListener(v -> {
            // TODO: Открыть экран редактирования профиля
            // startActivity(new Intent(requireContext(), EditProfileActivity.class));
        });

        // Клик на иконку "Поделиться"
//        binding.profileHeader.btnShareProfile.setOnClickListener(v -> {
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "Мой профиль: @" + binding.profileHeader.tvProfileUsername.getText());
//            startActivity(Intent.createChooser(shareIntent, "Поделиться профилем"));
//        });
    }

    private void setupTabsAndViewPager() {
        // Подключаем адаптер страниц к ViewPager2
        ProfilePagerAdapter pagerAdapter = new ProfilePagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);

        // Связываем TabLayout и ViewPager2, задаем названия вкладок
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Созданные");
            } else {
                tab.setText("Сохраненные");
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}