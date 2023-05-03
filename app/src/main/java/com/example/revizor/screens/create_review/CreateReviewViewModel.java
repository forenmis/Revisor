package com.example.revizor.screens.create_review;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.app.App;
import com.example.revizor.data.database.Database;
import com.example.revizor.entity.Review;
import com.example.revizor.entity.SendStatus;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateReviewViewModel extends AndroidViewModel {
    private final Database database;
    private final MutableLiveData<Boolean> createReviewLD = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public CreateReviewViewModel(@NonNull Application application) {
        super(application);
        database = App.getInstance(application.getApplicationContext()).getDatabase();
    }

    public void createDraft(String caption, String title, String photoUrl, String location, String date) {
        Review review = new Review();
        review.setCaption(caption);
        review.setTitle(title);
        review.setPhotoUrl(photoUrl);
        review.setLocation(location);
        review.setSendStatus(SendStatus.ReadyToSend);
        review.setDate(date);

        Disposable disposable = database.addReviewAsync(review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> createReviewLD.postValue(true)
                );
        compositeDisposable.add(disposable);
    }

    public LiveData<Boolean> getCreateReviewLD() {
        return createReviewLD;
    }

    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
