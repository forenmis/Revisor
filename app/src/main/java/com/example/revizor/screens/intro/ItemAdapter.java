package com.example.revizor.screens.intro;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revizor.databinding.ItemIntroBinding;
import com.example.revizor.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.VH> {
    private final List<Item> items = new ArrayList<>();


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemIntroBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item item = items.get(position);
        holder.binding.tvTitle.setText(item.getTitle());
        holder.setMyImage(holder.binding.ivImage, item.getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemIntroBinding binding;

        public VH(@NonNull ItemIntroBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setMyImage(ImageView view, int i) {
            view.setImageResource(i);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<Item> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }
}
