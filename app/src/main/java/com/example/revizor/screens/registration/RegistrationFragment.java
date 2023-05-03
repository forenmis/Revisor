package com.example.revizor.screens.registration;

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
import com.example.revizor.databinding.FragmentRegistrationBinding;
import com.example.revizor.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

public class RegistrationFragment extends BaseFragment<FragmentRegistrationBinding> {
    private RegistrationViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
    }


    @Override
    protected FragmentRegistrationBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentRegistrationBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ivBack.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(RegistrationFragment.this);
            NavDirections action = RegistrationFragmentDirections.actionRegistrationFragmentToAuthorizationFragment();
            navController.navigate(action);
        });
        binding.btCreateAccount.setOnClickListener(v -> {
            if (!Utils.checkPass(Utils.getText(binding.etPassword), Utils.getText(binding.etCheckPass))) {
                binding.etCheckPass.setError(getString(R.string.exception_check_password));
            } else {
                viewModel.registration(
                        Utils.getText(binding.etEmail),
                        Utils.getText(binding.etLogin),
                        Utils.getText(binding.etPassword)
                );
            }
        });

        viewModel.getProgressLD().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.btCreateAccount.setText(R.string.loading);
            } else {
                binding.btCreateAccount.setText(R.string.log);
            }
        });
        viewModel.getRegistrationLD().observe(getViewLifecycleOwner(), aBoolean -> {
            NavController navController = NavHostFragment.findNavController(RegistrationFragment.this);
            NavDirections action = RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment();
            navController.navigate(action);
        });
        viewModel.getExceptionLD().observe(getViewLifecycleOwner(),
                s -> Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_SHORT).show());
    }
}
