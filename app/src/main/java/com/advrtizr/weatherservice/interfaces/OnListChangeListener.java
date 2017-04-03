package com.advrtizr.weatherservice.interfaces;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.List;

public interface OnListChangeListener {
        void onListChanged(List<WeatherInfo> list);
}
