package com.example.diyproject.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentAlbumDetailBinding;
import com.example.diyproject.instructionDetail.InstructionDetailFragment;
import com.example.diyproject.model.AlbumInstructionItem;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailFragment extends Fragment {

    private static final String ARG_ALBUM_ID = "album_id";
    private static final String ARG_ALBUM_TITLE = "album_title";
    private static final String ARG_ALBUM_COUNT = "album_count";

    private FragmentAlbumDetailBinding binding;

    private int albumId;
    private String albumTitle;
    private int albumCount;

    public static AlbumDetailFragment newInstance(int albumId, String title, int count) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ALBUM_ID, albumId);
        args.putString(ARG_ALBUM_TITLE, title);
        args.putInt(ARG_ALBUM_COUNT, count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        binding = FragmentAlbumDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();




    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            albumId = getArguments().getInt(ARG_ALBUM_ID);
            albumTitle = getArguments().getString(ARG_ALBUM_TITLE, "Альбом");
            albumCount = getArguments().getInt(ARG_ALBUM_COUNT, 0);
        }

        binding.topBar.tvTopBarTitle.setText(albumTitle);
        binding.tvInstructionsCount.setText(albumCount + " инструкций");

        binding.topBar.btnBack.setOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
        );

        binding.topBar.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Альбом: " + albumTitle);
            startActivity(Intent.createChooser(shareIntent, "Поделиться альбомом"));
        });

        binding.rvAlbumInstructions.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        int spacingInDp = 10;
        int spacingInPx = Math.round(spacingInDp * getResources().getDisplayMetrics().density);
        binding.rvAlbumInstructions.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPx, false));


        List<AlbumInstructionItem> mockItems = new ArrayList<>();
        mockItems.add(new AlbumInstructionItem(1, "Брелок на ключи", R.drawable.album_image1));
        mockItems.add(new AlbumInstructionItem(2, "Брелок на ключи", R.drawable.album_image1));
        mockItems.add(new AlbumInstructionItem(3, "Брелок на ключи", R.drawable.album_image1));
        mockItems.add(new AlbumInstructionItem(4, "Брелок на ключи", R.drawable.album_image1));
        mockItems.add(new AlbumInstructionItem(5, "Брелок на ключи", R.drawable.album_image1));
        mockItems.add(new AlbumInstructionItem(6, "Брелок на ключи", R.drawable.album_image1));

        AlbumInstructionsAdapter adapter = new AlbumInstructionsAdapter(mockItems, item -> {
            Bundle args = new Bundle();
            args.putLong(InstructionDetailFragment.ARG_INSTRUCTION_ID, item.id);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.instructionDetailFragment, args);
        });

        binding.rvAlbumInstructions.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}