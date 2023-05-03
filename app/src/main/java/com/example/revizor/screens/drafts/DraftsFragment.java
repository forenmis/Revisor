package com.example.revizor.screens.drafts;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentDraftsBinding;
import com.example.revizor.entity.Review;
import com.example.revizor.screens.drafts.draft_details.DraftDetailFragment;
import com.example.revizor.screens.drafts.draft_details.OnDraftClickCallback;

public class DraftsFragment extends BaseFragment<FragmentDraftsBinding> {
    private DraftAdapter adapter;
    private DraftViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DraftAdapter();
        viewModel = new ViewModelProvider(this).get(DraftViewModel.class);
        adapter.setCallback(new OnDraftClickCallback() {
            @Override
            public void onDraftClick(Review review) {
                getParentFragmentManager().setFragmentResultListener(DraftDetailFragment.class.getName(),
                        DraftsFragment.this,
                        (requestKey, result) -> viewModel.loadReviewList());
                NavController controller = NavHostFragment
                        .findNavController(DraftsFragment.this);
                NavDirections action = DraftsFragmentDirections.
                        actionDraftsFragmentToDraftDetailFragment(review.getId());
                controller.navigate(action);
            }

            @Override
            public void onDraftSend(Review review) {
                viewModel.sendReview(review);
            }
        });
    }

    @Override
    protected FragmentDraftsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentDraftsBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvDrafts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvDrafts.setAdapter(adapter);
        binding.ivBack.setOnClickListener(view1 ->
                NavHostFragment.findNavController(DraftsFragment.this).popBackStack());
        viewModel.getReviewListLD().observe(getViewLifecycleOwner(), reviews ->
                adapter.updateItems(reviews));
    }
}
