package com.advrtizr.weatherservice.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;
import com.advrtizr.weatherservice.interfaces.WeatherRequest;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class WeatherCompiler implements WeatherModel{

    private Retrofit retrofit;
    private SharedPreferences savedWeatherData;

    public WeatherCompiler(Context context) {
        retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        savedWeatherData = context.getSharedPreferences(SettingsInteractorImpl.SETTINGS_PREF, MODE_PRIVATE);
    }

    @Override
    public void getWeather(final OnRequestFinishListener listener, final String location) {
        String unit = savedWeatherData.getString(SettingsInteractorImpl.UNIT, null);
        String YQL = Constants.LOCATION_PART + location + Constants.UNIT_PART + unit + Constants.END_PART;
        WeatherRequest request = retrofit.create(WeatherRequest.class);
        request.getWeatherInfo(YQL).enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if (response.body() != null) {
                    WeatherInfo weatherInfo = response.body();
                    int responseCount = weatherInfo.getQuery().getCount();
                    if (responseCount != 0) {
                        listener.onResult(weatherInfo);
                    } else {
                        getWeather(listener, location);
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }
}
