package com.example.revizor.screens.home.drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentDrawerBinding;
import com.example.revizor.screens.home.entity.DrawerSection;

public class DrawerFragment extends BaseFragment<FragmentDrawerBinding> {
    private DrawerViewModel drawerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerViewModel = new ViewModelProvider(requireActivity()).get(DrawerViewModel.class);
    }

    @Override
    protected FragmentDrawerBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentDrawerBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawerViewModel.getCurrentUserLD().observe(getViewLifecycleOwner(),
                user -> binding.tvLogin.setText(user.getLogin()));
        binding.cvReviews.setOnClickListener(v -> drawerViewModel.onSectionClick(DrawerSection.Reviews));
        binding.tvCloseDrawer.setOnClickListener(v -> drawerViewModel.openDrawer());
        binding.cvDrafts.setOnClickListener(v -> drawerViewModel.onSectionClick(DrawerSection.Drafts));
        binding.cvSettings.setOnClickListener(v -> drawerViewModel.onSectionClick(DrawerSection.Settings));

    }
}
