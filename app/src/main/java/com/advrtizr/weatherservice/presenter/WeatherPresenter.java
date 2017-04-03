package com.advrtizr.weatherservice.presenter;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.List;

public interface WeatherPresenter {
 void requestWeather();
 void saveData(List<WeatherInfo> infoList);
 List<WeatherInfo> loadWeather();
}
