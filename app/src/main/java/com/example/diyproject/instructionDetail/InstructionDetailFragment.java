package com.example.diyproject.instructionDetail;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diyproject.R;
import com.example.diyproject.databinding.FragmentInstructionDetailBinding;
import com.example.diyproject.model.InstructionStepItem;
import com.example.diyproject.model.MaterialItem;
import com.example.diyproject.model.StepMediaItem;
import com.google.android.material.chip.Chip;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class InstructionDetailFragment extends Fragment {
    public static final String ARG_INSTRUCTION_ID = "instruction_id";
    public static final String ARG_USER_ID = "user_id";
    public static final String ARG_AUTHOR_NAME = "author_name";

    private long authorId = -1L;
    private String authorName = "";
    private FragmentInstructionDetailBinding binding;
    private MaterialAdapter materialAdapter;
    private InstructionStepAdapter stepAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInstructionDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupClicks();
        setupLists();

        long instructionId = getArguments() != null
                ? getArguments().getLong(ARG_INSTRUCTION_ID, -1L)
                : -1L;

        // Загружаю тестовую интсрукцию
        loadSampleFrogInstruction(instructionId);
    }

    private void setupClicks() {
        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.btnLike.setOnClickListener(v -> v.setSelected(!v.isSelected()));
        binding.btnBookmark.setOnClickListener(v -> v.setSelected(!v.isSelected()));
        binding.boxAuthor.setOnClickListener(v -> openAuthorProfile(authorId, authorName));
    }

    private void setupLists() {
        materialAdapter = new MaterialAdapter((item, isChecked, position) -> {
            // Обновить наличие материала на складе пользователя в БД
        });
        binding.rvMaterials.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMaterials.setAdapter(materialAdapter);

        stepAdapter = new InstructionStepAdapter((mediaItem, position) -> {
            if (mediaItem.isVideo()) {
                // Открыть просмотр видео (до 30 сек)
            } else {
                // Открыть просмотр фото
            }
        });
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSteps.setAdapter(stepAdapter);
    }

    private void loadSampleFrogInstruction(long instructionId) {
        String drawableSchemeUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.post_image).toString();
        String avatarUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.profile_img).toString();

        long sampleAuthorId = 101L;
        String sampleAuthorName = "rukodelniza25";
        bindHeader(
                sampleAuthorId,
                sampleAuthorName,
                avatarUri,
                drawableSchemeUri,
                "Брелок лягушка",
                "Этот милый брелок в виде лягушки идеально подойдет как подвеска на ключи, сумку или рюкзак",
                "30 мин",
                "Брелок крючком"
        );

        bindHashtags(Arrays.asList("Брелок", "Вязаниекрючком", "Нитки", "Рукоделие","Дляновыичков"));

        bindMaterials(Arrays.asList(
                new MaterialItem("Зеленые нитки", true),
                new MaterialItem("Крючок 2,5 мм или 3 мм", false),
                new MaterialItem("Пластиковые глазки", true),
                new MaterialItem("Кольцо для брелока", false)
        ));

        List<InstructionStepItem> steps = Arrays.asList(
                new InstructionStepItem(
                        1,
                        "Подготавливаем материалы",
                        "Нам понадобятся нитки для вязания, крючок 2,5 мм или 3 мм, глазки и наполнитель.",
                        Collections.singletonList(
                                new StepMediaItem(drawableSchemeUri, StepMediaItem.TYPE_IMAGE, 0)
                        )
                ),
                new InstructionStepItem(
                        2,
                        "Вяжем туловище",
                        "Набираем кольцо амигуруми из 6 столбиков без накида и равномерно прибавляем петли в каждом ряду.",
                        Arrays.asList(
                                new StepMediaItem(drawableSchemeUri, StepMediaItem.TYPE_IMAGE, 0),
                                new StepMediaItem(drawableSchemeUri, StepMediaItem.TYPE_VIDEO, 24)
                        )
                )
        );
        bindSteps(steps);
    }

    public void bindHeader(long authorId,
                           String authorName,
                           String avatarUri,
                           String mainPhotoUri,
                           String title,
                           String description,
                           String duration,
                           String topic) {
        this.authorId = authorId;
        this.authorName = authorName;
        binding.tvAuthorName.setText(authorName);
        binding.tvTitle.setText(title);
        binding.tvDescription.setText(description);
        binding.chipDuration.setText(duration);
        binding.chipTopic.setText(topic);

        Glide.with(this)
                .load(avatarUri)
                .placeholder(R.drawable.profile_img)
                .error(R.drawable.profile_img)
                .centerCrop()
                .into(binding.ivAuthorAvatar);

        Glide.with(this)
                .load(mainPhotoUri)
                .placeholder(R.drawable.post_image)
                .error(R.drawable.post_image)
                .centerCrop()
                .into(binding.ivItemPhoto);
    }

    public void bindHashtags(List<String> hashtags) {
        binding.chipGroupHashtags.removeAllViews();

        if (hashtags == null || hashtags.isEmpty()) {
            binding.chipGroupHashtags.setVisibility(View.GONE);
            return;
        }

        binding.chipGroupHashtags.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (String tag : hashtags) {
            if (tag == null) continue;
            String cleanTag = tag.replace("#", "").trim();
            if (cleanTag.isEmpty()) continue;

            TextView tagView = (TextView) inflater.inflate(
                    R.layout.item_hashtag,
                    binding.chipGroupHashtags,
                    false
            );
            tagView.setText(getString(R.string.tag_hash_format, cleanTag));
            binding.chipGroupHashtags.addView(tagView);
        }


    }
    public void bindMaterials(List<MaterialItem> materials) {
        materialAdapter.setItems(materials);
    }

    public void bindSteps(List<InstructionStepItem> steps) {
        stepAdapter.setSteps(steps);
    }

    private void openAuthorProfile(long targetAuthorId, String targetAuthorName) {
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, targetAuthorId);
        args.putString(ARG_AUTHOR_NAME, targetAuthorName);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_instructionDetailFragment_to_userAccountFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}