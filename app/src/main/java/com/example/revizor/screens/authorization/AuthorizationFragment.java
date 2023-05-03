package com.example.revizor.screens.authorization;

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

import com.example.revizor.R;
import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentAuthorizationBinding;
import com.example.revizor.utils.Utils;
import com.google.android.material.snackbar.Snackbar;


public class AuthorizationFragment extends BaseFragment<FragmentAuthorizationBinding> {
    private AuthorizationViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthorizationViewModel.class);
    }

    @Override
    protected FragmentAuthorizationBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAuthorizationBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getExceptionLD().observe(getViewLifecycleOwner(), errorMessage -> {
            Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_SHORT).show();
        });
        viewModel.getProgressLD().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.btLogin.setText(R.string.loading);
            } else {
                binding.btLogin.setText(R.string.log);
            }
        });
        viewModel.getAuthLD().observe(getViewLifecycleOwner(), aBoolean -> {
            NavController navController = NavHostFragment.findNavController(AuthorizationFragment.this);
            NavDirections action = AuthorizationFragmentDirections.actionAuthorizationFragmentToHomeFragment();
            navController.navigate(action);
        });
        binding.btLogin.setOnClickListener(v -> {
            viewModel.signIn(Utils.getText(binding.etLogin),
                    Utils.getText(binding.etPassword));
        });
        binding.tvDontHaveAcc.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(AuthorizationFragment.this);
            NavDirections action = AuthorizationFragmentDirections.actionAuthorizationFragmentToRegistrationFragment();
            navController.navigate(action);
        });

    }
}
