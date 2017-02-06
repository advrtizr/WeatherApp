package com.advrtizr.weatherservice.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.WeatherCompiler;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.tv_location)
    TextView loc;
    @BindView(R.id.tv_temperature)
    TextView temperature;
    @BindView(R.id.tv_conditions)
    TextView conditions;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    private String country;
    private String city;
    private String unit;
    private String location;
    private WeatherCompiler weatherCompiler;
    private SharedPreferences weatherSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        loadFromSave();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherSettings = getSharedPreferences("weather_settings", MODE_PRIVATE);
                country = weatherSettings.getString("0", null);
                city = weatherSettings.getString("1", null);
                unit = weatherSettings.getString("2", null);
                location = city + ", " + country;
                weatherCompiler = new WeatherCompiler(WeatherActivity.this, location, unit);
                weatherCompiler.setWeatherFromRequest();
            }
        });
    }

    public void loadFromSave(){
        weatherSettings = getSharedPreferences("save_weather_data", MODE_PRIVATE);
        loc.setText(weatherSettings.getString("0", null));
        conditions.setText(weatherSettings.getString("1", null));
        temperature.setText(weatherSettings.getString("2", null));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
