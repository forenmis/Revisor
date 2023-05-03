package com.example.revizor.screens.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.revizor.app.App;
import com.example.revizor.app.AppPreferences;

public class SettingsViewModel extends AndroidViewModel {
    private final AppPreferences appPreferences;


    public SettingsViewModel(@NonNull Application application) {
        super(application);
        appPreferences = App.getInstance(application.getApplicationContext()).getAppPreferences();
    }

    public void exit() {
        appPreferences.clearToken();
    }
}
