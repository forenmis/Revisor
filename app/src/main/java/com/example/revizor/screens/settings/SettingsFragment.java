package com.example.revizor.screens.settings;

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
import com.example.revizor.databinding.FragmentSettingsBinding;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> {

    private SettingsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
    }

    @Override
    protected FragmentSettingsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingsBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvExit.setOnClickListener(view1 -> {
            viewModel.exit();
            NavController navController = NavHostFragment.findNavController(SettingsFragment.this);
            NavDirections action = SettingsFragmentDirections.actionSettingsFragmentToAuthorizationFragment();
            navController.navigate(action);
        });
        binding.ivBack.setOnClickListener(view1 ->
                NavHostFragment.findNavController(SettingsFragment.this).popBackStack());
    }
}
