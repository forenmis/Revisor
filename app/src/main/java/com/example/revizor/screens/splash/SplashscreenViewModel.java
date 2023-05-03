package com.example.revizor.screens.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.app.App;
import com.example.revizor.app.AppPreferences;
import com.example.revizor.entity.Action;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashscreenViewModel extends AndroidViewModel {
    private final AppPreferences appPreferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Action> actionLD = new MutableLiveData<>();


    public SplashscreenViewModel(@NonNull Application application) {
        super(application);
        appPreferences = App.getInstance(application.getApplicationContext()).getAppPreferences();
        navigate();
    }

    private Action getAction() {
        if (appPreferences.sessionToken() != null) {
            return Action.ToHome;
        } else if (appPreferences.isFirstLaunch()) {
            return Action.ToContainer;
        } else {
            return Action.ToAuthorization;
        }
    }

    private void navigate() {
        Disposable disposable = Single.just(true)
                .map(aBoolean -> getAction())
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> actionLD.postValue(action));
        compositeDisposable.add(disposable);
    }

    public LiveData<Action> getActionLD() {
        return actionLD;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
