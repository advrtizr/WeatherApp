package com.advrtizr.weatherservice.interfaces;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

public interface OnRequestFinishListener {
    void onResult(String id, WeatherInfo weatherInfo);
    void onFailure(Throwable t);
}
