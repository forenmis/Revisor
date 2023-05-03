package com.example.revizor.screens.drafts.draft_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.revizor.R;
import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentDraftDetailsBinding;
import com.example.revizor.entity.Review;
import com.example.revizor.entity.SendStatus;

public class DraftDetailFragment extends BaseFragment<FragmentDraftDetailsBinding> {
    private DraftDetailViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DraftDetailViewModel.class);
        DraftDetailFragmentArgs args = DraftDetailFragmentArgs.fromBundle(requireArguments());
        viewModel.loadReview(args.getId());
    }

    @Override
    protected FragmentDraftDetailsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentDraftDetailsBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getStatusReviewLD().observe(getViewLifecycleOwner(), sendStatus ->
                updStatus(sendStatus));
        viewModel.getReviewLD().observe(getViewLifecycleOwner(), review ->
                fillReview(review));
        binding.ivBack.setOnClickListener(v ->
                NavHostFragment.findNavController(DraftDetailFragment.this).popBackStack());
        binding.tvStatus.setOnClickListener(v -> {
            viewModel.sendReview(viewModel.getReviewLD().getValue());
            getParentFragmentManager().setFragmentResult(DraftDetailFragment.class.getName(), Bundle.EMPTY);
        });
    }

    private void setPhoto(Review review) {
        if (review.getPhotoUrl() != null) {
            binding.ivPhoto.setVisibility(View.VISIBLE);
            Glide.with(binding.ivPhoto)
                    .load(review.getPhotoUrl())
                    .into(binding.ivPhoto);
        } else {
            binding.ivPhoto.setVisibility(View.GONE);
        }
    }

    private void fillReview(Review review) {
        binding.tvTitle.setText(review.getTitle());
        binding.tvCaption.setText(review.getCaption());
        setPhoto(review);
        binding.tvLocation.setText(review.getLocation());
        binding.tvDate.setText(review.getDate());
        if (review.getSendStatus() != null) {
            binding.tvStatus.setVisibility(View.VISIBLE);
            updStatus(review.getSendStatus());
        } else {
            binding.tvStatus.setVisibility(View.GONE);
        }
        if (review.getAuthor() != null) {
            binding.tvAuthorName.setText(review.getAuthor());
        }
    }

    private void updStatus(SendStatus status) {
        if (status == SendStatus.ReadyToSend) {
            binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds
                    (0, 0, R.drawable.icon_ready_to_send, 0);
            binding.tvStatus.setText(R.string.ready_to_send);
        } else if (status == SendStatus.Sending) {
            binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds
                    (0, 0, R.drawable.icon_sending, 0);
            binding.tvStatus.setText(R.string.sending);
        } else {
            binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds
                    (0, 0, R.drawable.icon_ready_to_send, 0);
            binding.tvStatus.setText(R.string.sent);
        }
    }
}