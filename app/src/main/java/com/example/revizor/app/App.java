package com.example.revizor.app;

import android.app.Application;
import android.content.Context;

import com.example.revizor.data.database.Database;
import com.example.revizor.data.network.NetworkManager;

public class App extends Application {
    private Database database;
    private AppPreferences appPreferences;
    private NetworkManager networkManager;

    @Override
    public void onCreate() {
        super.onCreate();
        appPreferences = new AppPreferences(this);
        networkManager = new NetworkManager(this);
        database = new Database(this);
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public static App getInstance(Context context) {
        return ((App) context.getApplicationContext());
    }

    public AppPreferences getAppPreferences() {
        return appPreferences;
    }

    public Database getDatabase() {
        return database;
    }
}
