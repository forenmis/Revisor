package com.example.revizor.screens.intro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.revizor.app.App;
import com.example.revizor.app.AppPreferences;
import com.example.revizor.data.provider.ItemProvider;
import com.example.revizor.entity.Item;

import java.util.List;

public class IntroViewModel extends AndroidViewModel {
    private final AppPreferences appPreferences;
    private final MutableLiveData<List<Item>> introLD = new MutableLiveData<>();

    public IntroViewModel(@NonNull Application application) {
        super(application);
        appPreferences = App.getInstance(application.getApplicationContext()).getAppPreferences();
    }

    public void loadItemListVP() {
        introLD.postValue(ItemProvider.items());
    }

    public void turnOffIntro() {
        appPreferences.setFirstLaunch(false);
    }

    public LiveData<List<Item>> getIntroLD() {
        return introLD;
    }
}
