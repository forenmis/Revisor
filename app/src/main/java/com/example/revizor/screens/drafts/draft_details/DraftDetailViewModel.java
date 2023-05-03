package com.example.revizor.screens.drafts.draft_details;

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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DraftDetailViewModel extends AndroidViewModel {
    private final Database database;
    private final NetworkManager networkManager;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<String> authorNameLD = new MutableLiveData<>();
    private final MutableLiveData<Review> reviewLD = new MutableLiveData<>();
    private final MutableLiveData<SendStatus> statusReviewLD = new MutableLiveData<>();

    public DraftDetailViewModel(@NonNull Application application) {
        super(application);
        database = App.getInstance(application.getApplicationContext()).getDatabase();
        networkManager = App.getInstance(application.getApplicationContext()).getNetworkManager();
    }

    public void loadReview(long id) {
        Disposable disposable = database.getReviewByIdAsync(id)
                .subscribe(review -> reviewLD.postValue(review));
        compositeDisposable.add(disposable);
    }

    public LiveData<Review> getReviewLD() {
        return reviewLD;
    }

    public void sendReview(Review review) {
        Disposable disposable = networkManager.getCurrentUser()
                .concatMapCompletable(user ->
                        networkManager.postReview(
                                review.getCaption(),
                                review.getTitle(),
                                review.getLocation(),
                                user.getLogin()
                        ))
                .concatWith(database.changeStatusReviewAsync(review, SendStatus.Sent))
                .doOnSubscribe(disposable1 -> {
                    review.setSendStatus(SendStatus.Sending);
                    statusReviewLD.postValue(SendStatus.Sending);
                })
                .doOnError(disposable1 -> {
                    review.setSendStatus(SendStatus.ReadyToSend);
                    statusReviewLD.postValue(SendStatus.ReadyToSend);
                })
                .doOnComplete(() -> {
                    review.setSendStatus(SendStatus.Sent);
                    statusReviewLD.postValue(SendStatus.Sent);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public MutableLiveData<String> getAuthorNameLD() {
        return authorNameLD;
    }

    public LiveData<SendStatus> getStatusReviewLD() {
        return statusReviewLD;
    }
}
