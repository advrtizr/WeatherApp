package com.advrtizr.weatherservice.presenter;

import com.advrtizr.weatherservice.model.WeatherInfo;

public interface WeatherPresenter {
 void loadWeather();
 WeatherInfo getWeatherInfo();
}
