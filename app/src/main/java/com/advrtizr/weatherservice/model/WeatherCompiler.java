package com.advrtizr.weatherservice.model;

import android.util.Log;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;
import com.advrtizr.weatherservice.interfaces.WeatherRequest;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherCompiler implements WeatherModel{

    private OnRequestFinishListener listener;
    private List<String> locations;
    private List<String> keys;
    private String unit;
    private Retrofit retrofit;
    private List<WeatherInfo> responseList;
    private int counter;

    public WeatherCompiler(OnRequestFinishListener listener, List<String> keys, List<String> locations, String unit) {
        this.listener = listener;
        this.keys = keys;
        this.locations = locations;
        this.unit = unit;
        responseList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void performRequest(final int position, final String key, final String location){
        String YQL = Constants.LOCATION_PART + location + Constants.UNIT_PART + unit + Constants.END_PART;
        WeatherRequest request = retrofit.create(WeatherRequest.class);
        request.getWeatherInfo(YQL).enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if (response.body() != null) {
                    WeatherInfo weatherInfo = response.body();
                    weatherInfo.setKey(key);
                    int responseCount = weatherInfo.getQuery().getCount();
                    if (responseCount != 0) {
                        responseList.remove(position);
                        responseList.add(position, weatherInfo);
                        Log.i("comp", String.valueOf(position) + " " + location);
                        counter++;
                        if(locations.size() == counter)
                        listener.onResult(responseList);
                        Log.i("WC", "add");
                        Log.i("list", "counter in compiler " + String.valueOf(counter));
                    } else {
                        performRequest(position, key, location);
                        Log.i("list", "request count 0 for " + String.valueOf(location));
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    @Override
    public void requestWeather() {
        if(locations != null){
            Log.i("list", "list in compiler " + String.valueOf(locations.size()));
            for(int i = 0; i< locations.size(); i++){
                responseList.add(null);
                String key = keys.get(i);
                String location = locations.get(i);
                performRequest(i, key, location);
            }
        }
    }
}
