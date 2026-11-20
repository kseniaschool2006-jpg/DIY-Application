package com.example.diyproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

        setupHeaderListeners();

        setupTabsAndViewPager();

        return view;
    }

    private void loadProfileHeaderMock() {

        binding.profileHeader.tvProfileName.setText("Мария");
        binding.profileHeader.tvProfileUsername.setText("@rukodelniza25");
        binding.profileHeader.tvProfileStats.setText("5 подписчиков • 1 подписка");
        binding.profileHeader.ivProfileAvatar.setImageResource(R.drawable.profile_img);
    }

    private void setupHeaderListeners() {

        binding.profileHeader.btnEditProfile.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_profileFragment_to_editProfileFragment);

        });

        binding.profileHeader.btnShareProfile.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Мой профиль: @" + binding.profileHeader.tvProfileUsername.getText());
            startActivity(Intent.createChooser(shareIntent, "Поделиться профилем"));
        });
    }

    private void setupTabsAndViewPager() {

        ProfilePagerAdapter pagerAdapter = new ProfilePagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);

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