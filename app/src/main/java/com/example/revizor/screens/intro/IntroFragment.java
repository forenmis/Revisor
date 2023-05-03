package com.example.revizor.screens.intro;

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
import androidx.viewpager2.widget.ViewPager2;

import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentContainerIntroBinding;

public class IntroFragment extends BaseFragment<FragmentContainerIntroBinding> {
    private ItemAdapter adapter;
    private ViewPager2.OnPageChangeCallback callback;
    private IntroViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemAdapter();
        viewModel = new ViewModelProvider(this).get(IntroViewModel.class);
        viewModel.loadItemListVP();
    }

    @Override
    protected FragmentContainerIntroBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentContainerIntroBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                refreshBt(position);
            }
        };
        binding.vp.setAdapter(adapter);
        viewModel.getIntroLD().observe(getViewLifecycleOwner(), items -> adapter.updateItems(items));
        binding.btStart.setOnClickListener(v -> {
            NavController navcontroller = NavHostFragment
                    .findNavController(IntroFragment.this);
            NavDirections action = IntroFragmentDirections.actionContainerFragmentToAuthorizationFragment();
            viewModel.turnOffIntro();
            navcontroller.navigate(action);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.vp.registerOnPageChangeCallback(callback);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.vp.unregisterOnPageChangeCallback(callback);
    }

    private void refreshBt(int position) {
        if (position == adapter.getItemCount() - 1) {
            binding.btStart.setVisibility(View.VISIBLE);
        } else {
            binding.btStart.setVisibility(View.GONE);
        }
    }
}
