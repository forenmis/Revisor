package com.example.revizor.screens.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentSplashScreenBinding;
import com.example.revizor.entity.Action;


public class SplashscreenFragment extends BaseFragment<FragmentSplashScreenBinding> {
    private SplashscreenViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SplashscreenViewModel.class);
    }

    @Override
    protected FragmentSplashScreenBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSplashScreenBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getActionLD().observe(getViewLifecycleOwner(), action -> {
            NavController navController = NavHostFragment.findNavController(SplashscreenFragment.this);
            NavDirections navDirections = getActionNavDir(action);
            navController.navigate(navDirections);
        });
    }

    private NavDirections getActionNavDir(Action action) {
        switch (action) {
            case ToHome:
                return SplashscreenFragmentDirections.actionSplashscreenFragmentToHomeFragment();
            case ToContainer:
                return SplashscreenFragmentDirections.actionSplashscreenFragmentToContainerFragment();
            case ToAuthorization:
                return SplashscreenFragmentDirections.actionSplashscreenFragmentToAuthorizationFragment();
            default:
                throw new IllegalArgumentException();
        }
    }
}
