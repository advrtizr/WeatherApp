package com.advrtizr.weatherservice.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.R;

public class SettingsModelImpl implements SettingsModel {

    private SharedPreferences sharedPreferences;
    private Context context;

    public SettingsModelImpl(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.SETTINGS_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public String getSettings() {
        String defaultUnit = context.getResources().getString(R.string.settings_default_value);
        String unit= sharedPreferences.getString(Constants.UNIT, null);
        if (unit != null){
            return unit + Constants.DEGREE;
        }
        return defaultUnit;
    }

    @Override
    public void setSettings(String resource) {
        if(resource != null){
            sharedPreferences.edit().putString(Constants.UNIT, resource).apply();
        }
    }
}
