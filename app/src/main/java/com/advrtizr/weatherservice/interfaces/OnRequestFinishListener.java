package com.advrtizr.weatherservice.interfaces;

import com.advrtizr.weatherservice.model.WeatherInfo;

public interface OnRequestFinishListener {
    void onResult(WeatherInfo info);

    void onFailure(Throwable t);
}
