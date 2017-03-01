package com.advrtizr.weatherservice.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.R;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherCompiler {

    private TextView conditions;
    private TextView location;
    private TextView temperature;
    private ImageView conditionImage;
    private Context context;
    private String loc;
    private String tempUnit;
    private Retrofit retrofit;
    private ProgressDialog progressDialog;
    private SharedPreferences saveWeatherData;

    public WeatherCompiler(Context context, String loc, String tempUnit) {
        this.context = context;
        this.loc = loc;
        this.tempUnit = tempUnit;
        retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        ButterKnife.bind((Activity) context);
        conditions = (TextView) ((Activity) context).findViewById(R.id.tv_conditions);
        location = (TextView) ((Activity) context).findViewById(R.id.tv_location);
        temperature = (TextView) ((Activity) context).findViewById(R.id.tv_temperature);
        conditionImage = (ImageView) ((Activity) context).findViewById(R.id.iv_cond_icon);
        saveWeatherData = context.getSharedPreferences("save_weather_data", Context.MODE_PRIVATE);
    }

    public void setWeatherFromRequest() {

        String YQL = Constants.LOCATION_PART + loc + Constants.UNIT_PART + tempUnit.charAt(0) + Constants.END_PART;
        WeatherRequest request = retrofit.create(WeatherRequest.class);
        progressDialog.show();
        request.getWeatherInfo(YQL).enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if (response.body() != null) {
                    WeatherInfo weatherInfo = response.body();
                    int responseCount = weatherInfo.getQuery().getCount();
                    if (responseCount != 0) {
                        String units = weatherInfo.getQuery().getResults().getChannel().getUnits().getTemperature();
                        String city = weatherInfo.getQuery().getResults().getChannel().getLocation().getCity();
                        String country = weatherInfo.getQuery().getResults().getChannel().getLocation().getCountry();
                        int resource = context.getResources().getIdentifier("@drawable/icon_" + weatherInfo.getQuery()
                                .getResults().getChannel().getItem().getCondition().getCode(), null, context.getPackageName());
                        Drawable weatherImage = context.getResources().getDrawable(resource);
                        location.setText(city + ", " + country);
                        conditions.setText(weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getText());
                        temperature.setText(weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getTemp() + "\u00B0" + units);
                        conditionImage.setImageDrawable(weatherImage);
                        saveWeatherData.edit().putString("0", city + ", " + country).apply();
                        saveWeatherData.edit().putString("1", conditions.getText().toString()).apply();
                        saveWeatherData.edit().putString("2", temperature.getText().toString()).apply();
                        progressDialog.dismiss();
                    } else {
                        setWeatherFromRequest();
                    }
                } else {
                    Toast.makeText(context, "weather not found for such city", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Toast.makeText(context, "error " + t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    public void saveWeatherData(){
    }
}
