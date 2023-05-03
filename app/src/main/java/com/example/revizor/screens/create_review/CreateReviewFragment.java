package com.example.revizor.screens.create_review;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentCreateReviewBinding;
import com.example.revizor.screens.map.MapFragment;
import com.example.revizor.utils.FileUtils;
import com.example.revizor.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class CreateReviewFragment extends BaseFragment<FragmentCreateReviewBinding> {
    private String path;
    private String address;
    private CreateReviewViewModel viewModel;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreateReviewViewModel.class);
        ActivityResultContracts.StartActivityForResult contract =
                new ActivityResultContracts.StartActivityForResult();
        launcher = registerForActivityResult(contract, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                binding.tvTakePicture.setVisibility(View.INVISIBLE);
                setPhoto(path);
            }
        });
    }

    @Override
    protected FragmentCreateReviewBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCreateReviewBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getCreateReviewLD().observe(getViewLifecycleOwner(), aBoolean ->
                NavHostFragment.findNavController(CreateReviewFragment.this).popBackStack());
        binding.ivBack.setOnClickListener(view1 ->
                NavHostFragment.findNavController(CreateReviewFragment.this).popBackStack());
        binding.tvTakePicture.setOnClickListener(v -> startCamera());
        binding.ivPhoto.setOnClickListener(v -> startCamera());
        binding.ivDone.setOnClickListener(v -> {
            Date date = new Date();
            viewModel.createDraft(
                    Utils.getText(binding.etCaption),
                    Utils.getText(binding.etTitle),
                    path,
                    address,
                    Utils.getStrInDate(date)
            );
        });

        ActivityResultContracts.RequestMultiplePermissions contract = new ActivityResultContracts
                .RequestMultiplePermissions();
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(contract, result -> {
                    boolean fineLocationGranted = getOrDefault
                            (result, Manifest.permission.ACCESS_FINE_LOCATION);
                    boolean coarseLocationGranted = getOrDefault
                            (result, Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (fineLocationGranted && coarseLocationGranted) {
                        binding.tvAddLocation.setOnClickListener(view1 -> {
                            getParentFragmentManager().setFragmentResultListener(MapFragment.class.getName(),
                                    CreateReviewFragment.this, (requestKey, result1) -> {
                                        address = result1.getString(Utils.BUNDLE_KEY_ADDRESS);
                                        binding.tvAddLocation.setText(address);
                                    });
                            NavController navController = NavHostFragment
                                    .findNavController(CreateReviewFragment.this);
                            NavDirections action = CreateReviewFragmentDirections.actionCreateReviewFragmentToMapFragment();
                            navController.navigate(action);
                        });
                    }
                });
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION});
    }

    private void setPhoto(String path) {
        Glide.with(binding.ivPhoto)
                .load(path)
                .into(binding.ivPhoto);
    }

    private void startCamera() {
        try {
            File file = FileUtils.createTempFile(requireContext());
            path = file.getAbsolutePath();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = FileProvider.getUriForFile(requireContext(), "com.example.revizor.fileprovider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            launcher.launch(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean getOrDefault(Map<String, Boolean> result, String permission) {
        Boolean permissionGranted = result.get(permission);
        if (permissionGranted == null) {
            permissionGranted = false;
        }
        return permissionGranted;
    }
}
