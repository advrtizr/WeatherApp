package com.advrtizr.weatherservice.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
            case R.id.main_refresh:
                weatherSettings = getSharedPreferences(SettingsActivity.SETTINGS_PREF, MODE_PRIVATE);
                int unitResource = weatherSettings.getInt(SettingsActivity.UNIT, 0);
                unit = getResources().getResourceEntryName(unitResource);
                location = new SettingsActivity().setLocation(getSharedPreferences(LocationActivity.LOCATION_PREF, MODE_PRIVATE));
                weatherCompiler = new WeatherCompiler(WeatherActivity.this, location, unit);
                weatherCompiler.setWeatherFromRequest();
        }
        return super.onOptionsItemSelected(item);
    }
}
