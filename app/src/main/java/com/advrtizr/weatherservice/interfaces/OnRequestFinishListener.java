package com.advrtizr.weatherservice.interfaces;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.List;

public interface OnRequestFinishListener {
    void onResult(List<WeatherInfo> weatherInfo);
    void onFailure(Throwable t);
}
