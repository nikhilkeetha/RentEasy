package io.kodular.nsmarttechnologies6.RentEasy.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import io.kodular.nsmarttechnologies6.RentEasy.R;


public class Preference {

    private static Preference preference;
    private static SharedPreferences sharedPreferences;


    public static Preference getInstance(Context context) {
        if (preference == null) {
            preference = new Preference();
            sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        }
        return preference;
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putString(String key, String value) {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public void putInt(String key, int value) {
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putLong(String key, long value) {
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void remove(String key) {
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clearPreference() {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
