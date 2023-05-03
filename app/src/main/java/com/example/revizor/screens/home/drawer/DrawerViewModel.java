package com.example.revizor.screens.home.drawer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.app.App;
import com.example.revizor.data.network.NetworkManager;
import com.example.revizor.entity.User;
import com.example.revizor.screens.home.entity.DrawerSection;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DrawerViewModel extends AndroidViewModel {
    private final NetworkManager networkManager;
    private final MutableLiveData<User> currentUserLD = new MutableLiveData<>();
    private final MutableLiveData<DrawerSection> drawerSectionLD = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> statusDrawerLD = new MutableLiveData<>();

    public DrawerViewModel(@NonNull Application application) {
        super(application);
        networkManager = App.getInstance(application.getApplicationContext()).getNetworkManager();
        loadCurrentUser();
    }

    public void loadCurrentUser() {
        Disposable disposable = networkManager.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> currentUserLD.postValue(user));
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public void openDrawer() {
        statusDrawerLD.postValue(true);
    }

    public void clearStatusDrawer() {
        statusDrawerLD.postValue(false);
    }

    public void onSectionClick(DrawerSection section) {
        drawerSectionLD.postValue(section);
    }

    public void clearSectionLD() {
        drawerSectionLD.postValue(DrawerSection.None);
    }

    public LiveData<User> getCurrentUserLD() {
        return currentUserLD;
    }

    public LiveData<Boolean> getStatusDrawerLD() {
        return statusDrawerLD;
    }

    public LiveData<DrawerSection> getDrawerSectionLD() {
        return drawerSectionLD;
    }

}
