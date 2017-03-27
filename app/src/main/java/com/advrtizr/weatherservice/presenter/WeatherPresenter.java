package com.advrtizr.weatherservice.presenter;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

public interface WeatherPresenter {
 void loadWeather();
 WeatherInfo getWeatherInfo();
}
