package com.example.revizor.screens.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentReviewDetailsBinding;
import com.example.revizor.entity.Review;

public class ReviewDetailsFragment extends BaseFragment<FragmentReviewDetailsBinding> {
    private ReviewDetailsViewModel viewModel;
    private String objectId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ReviewDetailsViewModel.class);
        ReviewDetailsFragmentArgs args = ReviewDetailsFragmentArgs.fromBundle(requireArguments());
        objectId = args.getObjectId();
        viewModel.loadReview(objectId);
    }

    @Override
    protected FragmentReviewDetailsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentReviewDetailsBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getIsDeleteComplete().observe(getViewLifecycleOwner(), aBoolean -> {
            NavHostFragment.findNavController(ReviewDetailsFragment.this).popBackStack();
        });
        viewModel.getReviewLD().observe(getViewLifecycleOwner(), review -> fillReview(review));
        binding.ivBack.setOnClickListener(view1 ->
                NavHostFragment.findNavController(ReviewDetailsFragment.this).popBackStack());
        binding.ivDelete.setOnClickListener(view12 -> viewModel.deleteReview(objectId));
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
        if (review.getAuthor() != null) {
            binding.tvAuthorName.setText(review.getAuthor());
        }
    }

}
