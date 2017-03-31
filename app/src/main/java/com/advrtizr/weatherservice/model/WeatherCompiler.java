package com.advrtizr.weatherservice.model;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;
import com.advrtizr.weatherservice.interfaces.WeatherRequest;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherCompiler implements WeatherModel{

    private OnRequestFinishListener listener;
    private String location;
    private String unit;
    private Retrofit retrofit;
    private String id;

    public WeatherCompiler(OnRequestFinishListener listener, String id, String location, String unit) {
        this.listener = listener;
        this.id = id;
        this.location = location;
        this.unit = unit;
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void requestWeather() {
        String YQL = Constants.LOCATION_PART + location + Constants.UNIT_PART + unit + Constants.END_PART;
        WeatherRequest request = retrofit.create(WeatherRequest.class);
        request.getWeatherInfo(YQL).enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if (response.body() != null) {
                    WeatherInfo weatherInfo = response.body();
                    int responseCount = weatherInfo.getQuery().getCount();
                    if (responseCount != 0) {
                        listener.onResult(id, weatherInfo);
                    } else {
                        requestWeather();
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
