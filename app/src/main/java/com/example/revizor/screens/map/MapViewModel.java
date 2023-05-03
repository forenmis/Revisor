package com.example.revizor.screens.map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.data.location.LocationManager;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MapViewModel extends AndroidViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<LatLng> latLngLD = new MutableLiveData<>();
    private final LocationManager locationManager;
    private final MutableLiveData<String> addressLD = new MutableLiveData<>();

    public MapViewModel(@NonNull Application application) {
        super(application);
        locationManager = new LocationManager(application.getApplicationContext());
    }

    public void loadCurrentLocation() {
        Disposable disposable = locationManager.getCurrentLocation()
                .map(location -> new LatLng(location.getLatitude(), location.getLongitude()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(latLngLD::postValue);
        compositeDisposable.add(disposable);
    }

    public void getAddressFromCoordinate(LatLng latLng) {
        Disposable disposable = locationManager.getAddress(latLng)
                .subscribe(s -> addressLD.postValue(s));
        compositeDisposable.add(disposable);
    }

    public LiveData<LatLng> getLatLngLD() {
        return latLngLD;
    }

    public LiveData<String> getAddressLD() {
        return addressLD;
    }

    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
