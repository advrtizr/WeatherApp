package com.advrtizr.weatherservice.model;

import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;

public interface WeatherModel {
    void getWeather(OnRequestFinishListener listener, String location);
}
