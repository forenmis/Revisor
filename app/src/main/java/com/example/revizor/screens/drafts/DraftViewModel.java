package com.example.revizor.screens.drafts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.app.App;
import com.example.revizor.data.database.Database;
import com.example.revizor.data.network.NetworkManager;
import com.example.revizor.entity.Review;
import com.example.revizor.entity.SendStatus;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DraftViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Review>> reviewListLD = new MutableLiveData<>();
    private final Database database;
    private final NetworkManager networkManager;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DraftViewModel(@NonNull Application application) {
        super(application);
        database = App.getInstance(application.getApplicationContext()).getDatabase();
        networkManager = App.getInstance(application.getApplicationContext()).getNetworkManager();
        loadReviewList();
    }

    public void loadReviewList() {
        Disposable disposable = database.getAllReviewAsync()
                .subscribe(reviews -> reviewListLD.postValue(reviews));
        compositeDisposable.add(disposable);
    }

    public LiveData<List<Review>> getReviewListLD() {
        return reviewListLD;
    }

    public void sendReview(Review review) {

        Disposable disposable = networkManager.getCurrentUser()
                .concatMapCompletable(user -> networkManager.postReview(
                        review.getCaption(),
                        review.getTitle(),
                        review.getLocation(),
                        user.getLogin())
                )
                .concatWith(database.changeStatusReviewAsync(review, SendStatus.Sent))
                .doOnSubscribe(disposable1 -> {
                    review.setSendStatus(SendStatus.Sending);
                    reviewListLD.postValue(reviewListLD.getValue());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    review.setSendStatus(SendStatus.Sent);
                    reviewListLD.postValue(reviewListLD.getValue());
                }, throwable -> {
                    review.setSendStatus(SendStatus.ReadyToSend);
                    reviewListLD.postValue(reviewListLD.getValue());
                });
        compositeDisposable.add(disposable);
    }

    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
