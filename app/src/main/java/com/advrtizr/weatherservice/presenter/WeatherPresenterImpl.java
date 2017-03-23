package com.advrtizr.weatherservice.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;

import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;
import com.advrtizr.weatherservice.model.WeatherAdapter;
import com.advrtizr.weatherservice.model.WeatherCompiler;
import com.advrtizr.weatherservice.model.WeatherDatabase;
import com.advrtizr.weatherservice.model.WeatherInfo;
import com.advrtizr.weatherservice.model.WeatherModel;
import com.advrtizr.weatherservice.ui.LocationActivity;
import com.advrtizr.weatherservice.view.WeatherView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WeatherPresenterImpl implements WeatherPresenter, OnRequestFinishListener {

    private WeatherView view;
    private WeatherModel model;
    private String location;
    private WeatherInfo weatherInfo;

    public WeatherPresenterImpl(Context context, WeatherView view, String location) {
        this.view = view;
        this.location = location;
        this.model = new WeatherDatabase(context);
    }

    @Override
    public void loadWeather() {
        view.showProgress();
        model.getWeather(this, location);
    }

    @Override
    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    @Override
    public void onResult(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
        view.hideProgress();
        view.onRequestSuccess();
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.onRequestError(t);
    }
}
