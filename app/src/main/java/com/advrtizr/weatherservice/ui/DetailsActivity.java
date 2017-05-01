package com.advrtizr.weatherservice.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.ForecastAdapter;
import com.advrtizr.weatherservice.model.json.weather.Forecast;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.forecast_recycler)
    RecyclerView forecastRecycler;
    @BindView(R.id.details_temperature)
    TextView temperature;
    @BindView(R.id.details_location)
    TextView location;
    @BindView(R.id.details_conditions)
    TextView condition;
    @BindView(R.id.iv_details_conditions)
    ImageView conditionImage;
    @BindView(R.id.details_feels_temperature)
    TextView feelsTemperature;
    @BindView(R.id.details_wind_direction)
    TextView direction;
    @BindView(R.id.details_wind_speed)
    TextView speed;
    @BindView(R.id.details_humidity)
    TextView humidity;
    @BindView(R.id.details_pressure)
    TextView pressure;
    @BindView(R.id.details_sunrise)
    TextView sunrise;
    @BindView(R.id.details_sunset)
    TextView sunset;
    private ForecastAdapter forecastAdapter;
    private WeatherInfo weatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        weatherInfo = (WeatherInfo) getIntent().getSerializableExtra(Constants.WEATHER_DATA);
        initializeData();
        initializeAdapter();
    }

    private void initializeAdapter() {
        if (weatherInfo != null) {
            List<Forecast> forecastList = weatherInfo.getQuery().getResults().getChannel().getItem().getForecast();
            forecastAdapter = new ForecastAdapter(this, forecastList);
            LinearLayoutManager horizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            forecastRecycler.setLayoutManager(horizontalLayout);
            forecastRecycler.hasFixedSize();
            forecastRecycler.setAdapter(forecastAdapter);
        }
    }

    private void initializeData() {
        String locationCity = weatherInfo.getQuery().getResults().getChannel().getLocation().getCity();
        String locationCountry = weatherInfo.getQuery().getResults().getChannel().getLocation().getCountry();
        String locationSummary = locationCity + ", " + locationCountry;
        String temperatureQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getTemp() + "\u00B0";
        String conditionsQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getText();
        String codeQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getCode();
        String feelsTemperatureQuery = tempConverter(weatherInfo.getQuery().getResults().getChannel().getWind().getChill()) + "\u00B0";
        String directionQuery = windDirection(weatherInfo.getQuery().getResults().getChannel().getWind().getDirection());
        String speedQuery = weatherInfo.getQuery().getResults().getChannel().getWind().getSpeed();
        String speedUnits = weatherInfo.getQuery().getResults().getChannel().getUnits().getSpeed();
        String speedSummary = speedQuery +" " + speedUnits;
        String humidityQuery = weatherInfo.getQuery().getResults().getChannel().getAtmosphere().getHumidity();
        String pressureQuery = pressureDivide(weatherInfo.getQuery().getResults().getChannel().getAtmosphere().getPressure());
        String sunriseQuery = weatherInfo.getQuery().getResults().getChannel().getAstronomy().getSunrise();
        String sunsetQuery = weatherInfo.getQuery().getResults().getChannel().getAstronomy().getSunset();

        location.setText(locationSummary);
        temperature.setText(temperatureQuery);
        condition.setText(conditionsQuery);
        feelsTemperature.setText(feelsTemperatureQuery);
        direction.setText(directionQuery);
        speed.setText(speedSummary);
        humidity.setText(humidityQuery);
        pressure.setText(pressureQuery);
        sunrise.setText(sunriseQuery);
        sunset.setText(sunsetQuery);
        int resource = getResources().getIdentifier("@drawable/ic_" + codeQuery, null, getPackageName());
        conditionImage.setImageResource(resource);
    }

    private String tempConverter(String temperature) {
        String units = (weatherInfo.getQuery().getResults().getChannel().getUnits().getTemperature()).toLowerCase();
        if (units.equals(Constants.CELSIUS.toLowerCase())) {
            int t = Integer.parseInt(temperature);
            int result = (t - 32) * 5 / 9;
            return String.valueOf(result);
        }
        return temperature;
    }

    private String windDirection(String direction) {
        int d = Integer.parseInt(direction);
        if (d < 45) {
            return "N";
        } else if (d >= 45 && d < 90) {
            return "NE";
        } else if (d >= 90 && d < 135) {
            return "E";
        } else if (d >= 135 && d < 180) {
            return "SE";
        } else if (d >= 180 && d < 225) {
            return "S";
        } else if (d >= 225 && d < 270) {
            return "SW";
        } else if (d >= 270 && d < 315) {
            return "W";
        } else if (d >= 315 && d < 360) {
            return "NW";
        }
        return direction;
    }

    private String pressureDivide(String pressure) {
        float p = Float.parseFloat(pressure) / 1000;
        return String.valueOf(p);
    }
}
