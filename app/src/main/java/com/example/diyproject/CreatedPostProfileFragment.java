package com.example.diyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diyproject.databinding.FragmentCreatedPostProfileBinding;
import com.example.diyproject.model.PostItemProfile;

import java.util.ArrayList;
import java.util.List;


public class CreatedPostProfileFragment extends Fragment {

    private FragmentCreatedPostProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreatedPostProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));


        int spacingInDp = 10;
        int spacingInPx = Math.round(spacingInDp *getResources().getDisplayMetrics().density);


        binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPx, true));

        List<PostItemProfile> mockList = new ArrayList<>();


        mockList.add(new PostItemProfile(1, R.drawable.post_image));
        mockList.add(new PostItemProfile(2, R.drawable.post_image));
        mockList.add(new PostItemProfile(3, R.drawable.post_image));
        mockList.add(new PostItemProfile(4, R.drawable.post_image));

        CreatedPostAdapter adapter = new CreatedPostAdapter(mockList, new CreatedPostAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(PostItemProfile item) {
                // Здесь будет логика перехода на экран редактирования при нажатии на карандаш
            }
        });

        binding.recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}