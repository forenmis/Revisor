package com.example.revizor.screens.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.app.App;
import com.example.revizor.app.AppPreferences;
import com.example.revizor.data.database.Database;
import com.example.revizor.data.network.NetworkManager;
import com.example.revizor.entity.Review;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class HomeViewModel extends AndroidViewModel {

    private final AppPreferences appPreferences;
    private final NetworkManager networkManager;
    private final MutableLiveData<List<Review>> reviewListLD = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Database database;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        networkManager = App.getInstance(application.getApplicationContext()).getNetworkManager();
        appPreferences = App.getInstance(application.getApplicationContext()).getAppPreferences();
        database = App.getInstance(application.getApplicationContext()).getDatabase();
    }

    public void loadReviewList() {
        Disposable disposable = networkManager.getAllReviews(appPreferences.sessionToken())
                .subscribe(reviews -> reviewListLD.postValue(reviews));
        compositeDisposable.add(disposable);
    }

    public MutableLiveData<List<Review>> getReviewListLD() {
        return reviewListLD;
    }

    public void exit() {
        appPreferences.clearToken();
    }

    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
