package com.advrtizr.weatherservice.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherRequest {

    @GET
    Call<WeatherInfo> getWeatherInfo(@Url String url);
}
