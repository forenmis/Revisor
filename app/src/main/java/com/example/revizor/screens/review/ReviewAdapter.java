package com.example.revizor.screens.review;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.revizor.databinding.ItemReviewBinding;
import com.example.revizor.entity.Review;
import com.example.revizor.screens.home.entity.OnReviewClickCallback;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.VH> {
    private final List<Review> reviews = new ArrayList<>();
    private OnReviewClickCallback callback;

    @NonNull
    @Override
    public ReviewAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewAdapter.VH(ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.VH holder, int position) {
        Review review = reviews.get(position);
        holder.fillReview(review);
        holder.binding.getRoot().setOnClickListener(view -> callback.onReviewClick(review.getObjectId()));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;

        public VH(@NonNull ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setPhoto(Review review) {
            Glide.with(binding.ivPhoto)
                    .load(review.getPhotoUrl())
                    .into(binding.ivPhoto);
        }

        private void fillReview(Review review) {
            binding.tvTitle.setText(review.getTitle());
            binding.tvCaption.setText(review.getCaption());
            if (review.getPhotoUrl() != null) {
                binding.ivPhoto.setVisibility(View.VISIBLE);
                setPhoto(review);
            } else {
                binding.ivPhoto.setVisibility(View.GONE);
            }

            binding.tvDate.setText(review.getDate());
            if (review.getAuthor() != null) {
                binding.tvAuthorName.setText(review.getAuthor());
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<Review> newList) {
        reviews.clear();
        reviews.addAll(newList);
        notifyDataSetChanged();
    }

    public void setCallback(OnReviewClickCallback callback) {
        this.callback = callback;
    }
}