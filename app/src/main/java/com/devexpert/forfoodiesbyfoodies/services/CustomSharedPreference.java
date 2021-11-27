package com.devexpert.forfoodiesbyfoodies.services;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomSharedPreference {
    private static CustomSharedPreference preference;
    private final SharedPreferences sharedPreferences;

    public static CustomSharedPreference getInstance(Context context) {
        if (preference == null) {
            preference = new CustomSharedPreference(context);
        }
        return preference;
    }

    private CustomSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("FFBFPreference", Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
