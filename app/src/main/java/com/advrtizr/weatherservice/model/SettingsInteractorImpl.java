package com.advrtizr.weatherservice.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.advrtizr.weatherservice.R;

public class SettingsInteractorImpl implements SettingsInteractor {

    static final String SETTINGS_PREF = "settings_pref";
    static final String UNIT = "unit";
    static final String DEGREE = "\u00B0";
    private SharedPreferences sharedPreferences;
    private Context context;

    public SettingsInteractorImpl(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public String getSettings() {
        String defaultUnit = context.getResources().getString(R.string.settings_default_value);
        String unit= sharedPreferences.getString(UNIT, null);
        if (unit != null){
            return unit + DEGREE;
        }
        return defaultUnit;
    }

    @Override
    public void setSettings(String resource) {
        if(resource != null){
            sharedPreferences.edit().putString(UNIT, resource).apply();
        }
    }
}
