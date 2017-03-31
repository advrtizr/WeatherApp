package com.advrtizr.weatherservice.view;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.List;
import java.util.Map;

public interface WeatherView {

    void showProgress();
    void hideProgress();
    void onRequestSuccess(List<WeatherInfo> list);
    void onRequestError(List<WeatherInfo> list);
}
