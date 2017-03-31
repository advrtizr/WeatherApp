package com.advrtizr.weatherservice.presenter;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.List;
import java.util.Map;

public interface WeatherPresenter {
 void requestWeather();
 List<WeatherInfo> loadWeather();
 void deleteItem(int position);
 void moveItem(int position, int target);


}
