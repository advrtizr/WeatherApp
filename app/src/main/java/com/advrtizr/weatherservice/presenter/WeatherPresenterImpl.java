package com.advrtizr.weatherservice.presenter;

import android.content.Context;

import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;
import com.advrtizr.weatherservice.model.WeatherCompiler;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;
import com.advrtizr.weatherservice.model.WeatherModel;
import com.advrtizr.weatherservice.view.WeatherView;

public class WeatherPresenterImpl implements WeatherPresenter, OnRequestFinishListener {

    private WeatherView view;
    private WeatherModel model;
    private String location;
    private WeatherInfo weatherInfo;

    public WeatherPresenterImpl(Context context, WeatherView view, String location) {
        this.view = view;
        this.location = location;
        this.model = new WeatherCompiler(context);
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
    public void onResult(Object obj) {
        this.weatherInfo = (WeatherInfo) obj;
        view.hideProgress();
        view.onRequestSuccess();
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        view.onRequestError(t);
    }
}
