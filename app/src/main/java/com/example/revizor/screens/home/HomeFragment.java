package com.example.revizor.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentReviewBinding;
import com.example.revizor.screens.home.drawer.DrawerViewModel;
import com.example.revizor.screens.home.entity.DrawerSection;
import com.example.revizor.screens.review.ReviewAdapter;

public class HomeFragment extends BaseFragment<FragmentReviewBinding> {
    private HomeViewModel homeViewModel;
    private DrawerViewModel drawerViewModel;
    private ReviewAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        drawerViewModel = new ViewModelProvider(requireActivity()).get(DrawerViewModel.class);
        adapter = new ReviewAdapter();

        homeViewModel.loadReviewList();
        adapter.setCallback(objectId -> {
            NavController controller = NavHostFragment.findNavController(HomeFragment.this);
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToReviewDetailsFragment(objectId);
            controller.navigate(action);
        });
    }

    @Override
    protected FragmentReviewBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentReviewBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.ivExit.setOnClickListener(v -> {
            homeViewModel.exit();
            NavController navController = NavHostFragment.findNavController(HomeFragment.this);
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToAuthorizationFragment();
            navController.navigate(action);
        });
        binding.ivOpenDrawer.setOnClickListener(v -> binding.getRoot().openDrawer(GravityCompat.START));
        binding.btCreateReview.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(HomeFragment.this);
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToCreateReviewFragment();
            navController.navigate(action);
        });

        drawerViewModel.getDrawerSectionLD().observe(getViewLifecycleOwner(), section -> {
            if (section != DrawerSection.None) {
                if (section == DrawerSection.Reviews) {
                    binding.getRoot().closeDrawer(GravityCompat.START);
                } else {
                    NavController navController = NavHostFragment.findNavController(HomeFragment.this);
                    NavDirections action = openSection(section);
                    navController.navigate(action);
                }
                drawerViewModel.clearSectionLD();
            }

        });
        drawerViewModel.getStatusDrawerLD().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean)
                binding.getRoot().closeDrawer(GravityCompat.START);
            drawerViewModel.clearStatusDrawer();
        });

        binding.rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvReviews.setAdapter(adapter);

        homeViewModel.getReviewListLD().observe(getViewLifecycleOwner(), reviews -> {
            adapter.updateItems(reviews);
            binding.swiperefresh.setRefreshing(false);
        });

        binding.swiperefresh.setOnRefreshListener(() -> {
            homeViewModel.loadReviewList();
        });
    }

    private NavDirections openSection(DrawerSection section) {
        switch (section) {
            case Drafts:
                return HomeFragmentDirections.actionHomeFragmentToDraftsFragment();
            case Settings:
                return HomeFragmentDirections.actionHomeFragmentToSettingsFragment();
            default:
                throw new IllegalArgumentException();
        }
    }
}
