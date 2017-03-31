package com.advrtizr.weatherservice.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;
import com.advrtizr.weatherservice.model.SettingsInteractorImpl;
import com.advrtizr.weatherservice.model.WeatherCompiler;
import com.advrtizr.weatherservice.model.WeatherModel;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;
import com.advrtizr.weatherservice.view.WeatherView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WeatherPresenterImpl implements WeatherPresenter, OnRequestFinishListener {

    private SharedPreferences unitPref;
    private SharedPreferences locationPref;
    private WeatherView view;
    private List<String> ids;
    private List<WeatherModel> models;
    private List<WeatherInfo> response;
    private Context context;
    private SharedPreferences pref;

    private final static String SAVED_RESPONSE = "saved_response";

    public WeatherPresenterImpl(WeatherView view, SharedPreferences locationPref) {
        this.view = view;
        context = ((Activity) view).getBaseContext();
        ids = new ArrayList<>();
        models = new ArrayList<>();
        response = new ArrayList<>();
        pref = context.getSharedPreferences(SAVED_RESPONSE, MODE_PRIVATE);
        this.locationPref = locationPref;
        unitPref = context.getSharedPreferences(SettingsInteractorImpl.SETTINGS_PREF, MODE_PRIVATE);
    }

    @Override
    public void requestWeather() {
        view.showProgress();
        response.clear();
        String unit = unitPref.getString(SettingsInteractorImpl.UNIT, null);
        Map<String, ?> locationList = locationPref.getAll();
        for (Map.Entry location : locationList.entrySet()) {
            if (location == null) {
                return;
            }
            String id = location.getKey().toString();
            String city = location.getValue().toString();
            Log.i("map", id + " " + city);
            WeatherModel model = new WeatherCompiler(this, id, city, unit);
            models.add(model);
            model.requestWeather();
        }
    }

    @Override
    public void onResult(String id, WeatherInfo weatherInfo) {
        if (weatherInfo != null) {
            ids.add(id);
            response.add(weatherInfo);
        }
        if (response.size() == models.size()) {
            view.hideProgress();
            view.onRequestSuccess(response);
            saveData();
        }
        Log.i("map", "models: " + String.valueOf(models.size() +
                ", response: " + String.valueOf(response.size())+
                ", pref: " + String.valueOf(locationPref.getAll().size())));
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.onRequestError(loadWeather());
    }

    private void saveData() {
        String json = new Gson().toJson(response);
        pref.edit().putString("response", json).apply();
    }

    @Override
    public void deleteItem(int position) {
        Log.i("map", "position " + String.valueOf(position));
        if(ids.size() != 0){
            locationPref.edit().remove(ids.get(position)).apply();
            ids.remove(position);
            response.remove(position);
        }
        saveData();
        Log.i("map", "models: " + String.valueOf(models.size() +
                ", response: " + String.valueOf(response.size())+
                ", pref: " + String.valueOf(locationPref.getAll().size())));
    }

    @Override
    public void moveItem(int position, int target) {
        WeatherInfo info = response.get(position);
        response.remove(position);
        response.add(target, info);
        saveData();
    }

    @Override
    public List<WeatherInfo> loadWeather() {
        String json = pref.getString("response", null);
        return new Gson().fromJson(json, new TypeToken<ArrayList<WeatherInfo>>() {
        }.getType());
    }

}
