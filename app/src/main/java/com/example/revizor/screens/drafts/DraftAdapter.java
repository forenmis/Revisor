package com.example.revizor.screens.drafts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.revizor.R;
import com.example.revizor.databinding.ItemDraftBinding;
import com.example.revizor.entity.Review;
import com.example.revizor.entity.SendStatus;
import com.example.revizor.screens.drafts.draft_details.OnDraftClickCallback;

import java.util.ArrayList;
import java.util.List;

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.VH> {
    private final List<Review> reviews = new ArrayList<>();
    private OnDraftClickCallback callback;

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemDraftBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Review review = reviews.get(position);
        holder.fillReview(review);
        holder.binding.getRoot().setOnClickListener(view -> callback.onDraftClick(review));
        holder.binding.tvStatus.setOnClickListener(view -> callback.onDraftSend(review));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemDraftBinding binding;

        public VH(@NonNull ItemDraftBinding binding) {
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
            updStatus(review);
            if (review.getAuthor() != null) {
                binding.tvAuthorName.setText(review.getAuthor());
            }
        }

        private void updStatus(Review review) {
            binding.tvStatus.setVisibility(View.VISIBLE);
            if (review.getSendStatus() == SendStatus.ReadyToSend) {
                binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.icon_ready_to_send, 0);
                binding.tvStatus.setText(R.string.ready_to_send);
            } else if (review.getSendStatus() == SendStatus.Sending) {
                binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.icon_sending, 0);
                binding.tvStatus.setText(R.string.sending);
            } else {
                binding.tvStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.icon_ready_to_send, 0);
                binding.tvStatus.setText(R.string.sent);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<Review> newList) {
        reviews.clear();
        reviews.addAll(newList);
        notifyDataSetChanged();
    }

    public void setCallback(OnDraftClickCallback callback) {
        this.callback = callback;
    }
}
