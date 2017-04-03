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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WeatherPresenterImpl implements WeatherPresenter, OnRequestFinishListener {

    private SharedPreferences unitPref;
    private SharedPreferences locationPref;
    private WeatherView view;
    private Context context;
    private SharedPreferences pref;

    private final static String SAVED_RESPONSE = "saved_response";

    public WeatherPresenterImpl(WeatherView view, SharedPreferences locationPref) {
        this.view = view;
        context = ((Activity) view).getBaseContext();
        pref = context.getSharedPreferences(SAVED_RESPONSE, MODE_PRIVATE);
        this.locationPref = locationPref;
        unitPref = context.getSharedPreferences(SettingsInteractorImpl.SETTINGS_PREF, MODE_PRIVATE);
    }

    @Override
    public void requestWeather() {
        String unit = unitPref.getString(SettingsInteractorImpl.UNIT, null);
        Map<String, ?> locationList = locationPref.getAll();
        List<String> loc = new ArrayList<>();
        for (Map.Entry location : locationList.entrySet()) {
            if (location == null) {
                return;
            }
            String city = location.getValue().toString();
            loc.add(city);
        }
        WeatherModel model = new WeatherCompiler(this, loc, unit);
        view.showProgress();
        model.requestWeather();
    }

    @Override
    public void onResult(List<WeatherInfo> infoList) {
        if (infoList != null) {
            saveData(sorted(infoList));
            view.hideProgress();
            view.onRequestSuccess(loadWeather());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.onRequestError(loadWeather());
    }
    @Override
    public void saveData(List<WeatherInfo> infoList) {
        String json = new Gson().toJson(infoList);
        pref.edit().putString("response", json).apply();
        Log.i("move", "moveItem");
    }

    @Override
    public List<WeatherInfo> loadWeather() {
        String json = pref.getString("response", null);
        return new Gson().fromJson(json, new TypeToken<ArrayList<WeatherInfo>>() {
        }.getType());
    }

    private List<WeatherInfo> sorted(List<WeatherInfo> infoList){
        List<WeatherInfo> matchedList = new ArrayList<>();
        List<WeatherInfo> restoredList = loadWeather();
        if(restoredList == null){
            return infoList;
        }
        for(int i = 0; i < restoredList.size(); i++){
            String restored = restoredList.get(i).getQuery().getResults().getChannel().getLocation().getCity();
            for(int j = 0; j < infoList.size(); j++){
                String response = infoList.get(j).getQuery().getResults().getChannel().getLocation().getCity();
                if(response.equals(restored)) {
                    Log.i("sort", String.valueOf(i) + " " + response + "+");
                    matchedList.add(infoList.get(j));
                    infoList.remove(j);
                }
            }
        }
        if(infoList.size() != 0){
            matchedList.addAll(infoList);
        }
        return matchedList;
    }
}
