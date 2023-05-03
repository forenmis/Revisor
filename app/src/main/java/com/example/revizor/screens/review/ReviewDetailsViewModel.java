package com.example.revizor.screens.review;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.app.App;
import com.example.revizor.app.AppPreferences;
import com.example.revizor.data.network.NetworkManager;
import com.example.revizor.entity.Review;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ReviewDetailsViewModel extends AndroidViewModel {

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final NetworkManager networkManager;
    private final MutableLiveData<Review> reviewLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isDeleteComplete = new MutableLiveData<>();
    private final AppPreferences appPreferences;


    public ReviewDetailsViewModel(@NonNull Application application) {
        super(application);
        networkManager = App.getInstance(application.getApplicationContext()).getNetworkManager();
        appPreferences = App.getInstance(application.getApplicationContext()).getAppPreferences();
    }

    public void loadReview(String objectId) {
        Disposable d = networkManager.getReviewDetails(objectId)
                .subscribe(review -> reviewLD.postValue(review));
        disposable.add(d);
    }

    public void deleteReview(String objectId) {
        Disposable d = networkManager.deleteReview(appPreferences.sessionToken(), objectId)
                .subscribe(() -> isDeleteComplete.postValue(true));
        disposable.add(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

    public MutableLiveData<Review> getReviewLD() {
        return reviewLD;
    }

    public MutableLiveData<Boolean> getIsDeleteComplete() {
        return isDeleteComplete;
    }
}
