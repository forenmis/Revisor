package com.example.revizor.screens.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.revizor.core.BaseFragment;
import com.example.revizor.databinding.FragmentMapBinding;
import com.example.revizor.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends BaseFragment<FragmentMapBinding> implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapViewModel viewModel;
    private LatLng latLng;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
    }

    @Override
    protected FragmentMapBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMapBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = binding.flMap.getFragment();
        supportMapFragment.getMapAsync(this);
        viewModel.loadCurrentLocation();
        viewModel.getLatLngLD().observe(getViewLifecycleOwner(), latLng -> {
            MapFragment.this.latLng = latLng;
            zoomToLocation();
        });
        viewModel.getAddressLD().observe(getViewLifecycleOwner(), s -> {
            Bundle bundle = new Bundle();
            bundle.putString(Utils.BUNDLE_KEY_ADDRESS, s);
            getParentFragmentManager().setFragmentResult(MapFragment.class.getName(), bundle);
            NavHostFragment.findNavController(MapFragment.this).popBackStack();
        });
        binding.ivBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(MapFragment.this).popBackStack();
        });
        binding.ivDone.setOnClickListener(v -> {
            if (googleMap != null) {
                LatLng latLng = googleMap.getCameraPosition().target;
                viewModel.getAddressFromCoordinate(latLng);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        zoomToLocation();
    }

    private void zoomToLocation() {
        if (latLng != null && googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    }
}
