package com.example.diyproject.profile;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentUserAccountBinding;
import com.example.diyproject.model.PostItemProfile;
import com.example.diyproject.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class UserAccountFragment extends Fragment {

    private FragmentUserAccountBinding binding;
    private UserProfile currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserAccount();
        setupListeners();
        setupCreatedItems();
    }


    private void loadUserAccount() {
        binding.topbarUserAccount.tvTopBarTitle.setText("");
        binding.topbarUserAccount.tvTopBarTitle.setVisibility(View.GONE);

        currentUser = new UserProfile(
                1,
                "Мария",
                "rukodelniza25",
                R.drawable.profile_img,
                5,
                1,
                false
        );

        bindUserProfile(currentUser);
    }

    private void bindUserProfile(UserProfile user) {
        if (binding == null || user == null) {
            return;
        }

        binding.tvUserName.setText(user.getName());
        binding.tvUserHandle.setText("@" + user.getUsername());
        binding.ivUserAvatar.setImageResource(user.getAvatarResId());

        updateFollowUi(user);
    }


    private void updateFollowUi(UserProfile user) {
        String stats = user.getFollowersCount() + " подписчиков • " + user.getFollowingCount() + " подписка";
        binding.tvFollowStats.setText(stats);

        com.google.android.material.button.MaterialButton btnFollow =
                (com.google.android.material.button.MaterialButton) binding.btnFollow;

        int greenColor = ContextCompat.getColor(requireContext(), R.color.color_green);
        int whiteColor = ContextCompat.getColor(requireContext(), R.color.color_white);
        int strokePx = (int) (1 * getResources().getDisplayMetrics().density);

        if (user.isSubscribed()) {
            btnFollow.setText("Вы подписаны");
            btnFollow.setBackgroundTintList(ColorStateList.valueOf(whiteColor));
            btnFollow.setTextColor(greenColor);
            btnFollow.setStrokeWidth(strokePx);
            btnFollow.setStrokeColor(ColorStateList.valueOf(greenColor));
        } else {
            btnFollow.setText("Подписаться");
            btnFollow.setBackgroundTintList(ColorStateList.valueOf(greenColor));
            btnFollow.setTextColor(whiteColor);
            btnFollow.setStrokeWidth(0);
        }
    }

    private void setupListeners() {
        binding.topbarUserAccount.btnBack.setOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
        );

        binding.topbarUserAccount.btnShare.setOnClickListener(v -> {
            String handle = currentUser != null ? currentUser.getUsername() : "";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Профиль пользователя: @" + handle);
            startActivity(Intent.createChooser(shareIntent, "Поделиться профилем"));
        });

        binding.btnFollow.setOnClickListener(v -> {
            if (currentUser == null) {
                return;
            }
            boolean nextSubscribed = !currentUser.isSubscribed();
            int nextFollowers = currentUser.getFollowersCount() + (nextSubscribed ? 1 : -1);

            currentUser.setSubscribed(nextSubscribed);
            currentUser.setFollowersCount(Math.max(0, nextFollowers));

            updateFollowUi(currentUser);
        });
    }

    private void setupCreatedItems() {
        List<PostItemProfile> items = new ArrayList<>();
        items.add(new PostItemProfile(1, R.drawable.post_image));
        items.add(new PostItemProfile(2, R.drawable.post_image));
        items.add(new PostItemProfile(3, R.drawable.post_image));
        items.add(new PostItemProfile(4, R.drawable.post_image));

        UserCreatedAdapter adapter = new UserCreatedAdapter(items, item ->
                //TODO Открытие инструкции
                Toast.makeText(requireContext(), "Работа #" + item.getId(), Toast.LENGTH_SHORT).show()
        );

        binding.rvCreatedItems.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        int spacingInDp = 10;
        int spacingInPx = Math.round(spacingInDp *getResources().getDisplayMetrics().density);


        binding.rvCreatedItems.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPx, false));
        binding.rvCreatedItems.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}