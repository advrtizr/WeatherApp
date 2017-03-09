package com.advrtizr.weatherservice.view;

import com.advrtizr.weatherservice.model.WeatherInfo;

public interface WeatherView {

    void displayWeather(WeatherInfo info);
    void showProgress();
    void hideProgress();
    void onRequestError(Throwable t);
}
