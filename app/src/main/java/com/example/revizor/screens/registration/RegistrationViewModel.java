package com.example.revizor.screens.registration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.R;
import com.example.revizor.app.App;
import com.example.revizor.data.network.NetworkManager;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegistrationViewModel extends AndroidViewModel {
    private final NetworkManager networkManager;
    private final MutableLiveData<Boolean> registrationLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> progressLD = new MutableLiveData<>();
    private final MutableLiveData<String> exceptionLD = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        networkManager = App.getInstance(application.getApplicationContext()).getNetworkManager();
    }

    public void registration(String email, String login, String password) {
        Disposable disposable = networkManager.registration(login, email, password)
                .doOnSubscribe(disposable1 -> progressLD.postValue(true))
                .doFinally(() -> progressLD.postValue(false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> registrationLD.postValue(true), throwable -> {
                    if (throwable instanceof UnknownHostException) {
                        exceptionLD.postValue(getApplication()
                                .getString(R.string.exception_internet_connection));
                    } else if (throwable instanceof SocketTimeoutException) {
                        exceptionLD.postValue(getApplication()
                                .getString(R.string.exception_socket_timeout));
                    } else {
                        exceptionLD.postValue(getApplication()
                                .getString(R.string.exception_else));
                    }
                });
        compositeDisposable.add(disposable);
    }

    public LiveData<Boolean> getRegistrationLD() {
        return registrationLD;
    }

    public LiveData<Boolean> getProgressLD() {
        return progressLD;
    }

    public LiveData<String> getExceptionLD() {
        return exceptionLD;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
