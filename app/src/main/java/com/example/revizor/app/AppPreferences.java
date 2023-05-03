package com.example.revizor.app;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String APP_PREFERENCES_NAME = "app.preferences";
    private static final String IS_TOKEN_KEY = "token_key";
    private static final String IS_VISIBLE_FIRST_SCREEN = "visible_first";
    private final SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(IS_VISIBLE_FIRST_SCREEN, true);
    }

    public void setFirstLaunch(boolean value) {
        sharedPreferences
                .edit()
                .putBoolean(IS_VISIBLE_FIRST_SCREEN, value)
                .apply();
    }

    public String sessionToken() {
        return sharedPreferences.getString(IS_TOKEN_KEY, null);
    }

    public void saveSessionToken(String token) {
        sharedPreferences
                .edit()
                .putString(IS_TOKEN_KEY, token)
                .apply();
    }

    public void clearToken() {
        sharedPreferences
                .edit()
                .remove(IS_TOKEN_KEY)
                .apply();
    }
}
