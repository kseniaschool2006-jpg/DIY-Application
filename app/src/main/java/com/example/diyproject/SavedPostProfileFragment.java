package com.example.diyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diyproject.databinding.FragmentSavedPostProfileBinding;
import com.example.diyproject.model.AlbumItemProfile;

import java.util.ArrayList;
import java.util.List;


public class SavedPostProfileFragment extends Fragment {

 private FragmentSavedPostProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSavedPostProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        int spacingInDp = 10;
        int spacingInPx = Math.round(spacingInDp *getResources().getDisplayMetrics().density);

        binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPx, false));

        List<AlbumItemProfile> mockAlbums = new ArrayList<>();

        mockAlbums.add(new AlbumItemProfile(
                1,
                "Брелоки",
                12,
                R.drawable.album_image1,
                R.drawable.album_image1,
                R.drawable.album_image1
        ));

        mockAlbums.add(new AlbumItemProfile(
                2,
                "Вязание крючком",
                12,
                R.drawable.album_image1,
                R.drawable.album_image1,
                R.drawable.album_image1
        ));

        mockAlbums.add(new AlbumItemProfile(true));

        SavedPostAdapter adapter = new SavedPostAdapter(mockAlbums, new SavedPostAdapter.OnAlbumClickListener() {
            @Override
            public void onAlbumClick(AlbumItemProfile album) {
                Bundle args = new Bundle();
                args.putInt("album_id", album.id);
                args.putString("album_title", album.title);
                args.putInt("album_count", album.count);

                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_profileFragment_to_albumDetailFragment, args);
            }

            @Override
            public void onAddClick() {
                // TODO: Здесь будет логика открытия диалога или экрана создания нового альбома
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