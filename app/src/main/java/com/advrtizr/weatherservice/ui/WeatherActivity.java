package com.advrtizr.weatherservice.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.WeatherCompiler;
import com.advrtizr.weatherservice.model.WeatherInfo;
import com.advrtizr.weatherservice.presenter.WeatherPresenter;
import com.advrtizr.weatherservice.presenter.WeatherPresenterImpl;
import com.advrtizr.weatherservice.view.WeatherView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherView {

    @BindView(R.id.tv_location)
    TextView loc;
    @BindView(R.id.tv_temperature)
    TextView temperature;
    @BindView(R.id.tv_conditions)
    TextView conditions;
    @BindView(R.id.iv_cond_icon)
    ImageView conditionImage;

    private WeatherPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        presenter = new WeatherPresenterImpl(this);
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
                presenter.loadWeather();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayWeather(WeatherInfo info) {
        String units = info.getQuery().getResults().getChannel().getUnits().getTemperature();
        String city = info.getQuery().getResults().getChannel().getLocation().getCity();
        String country = info.getQuery().getResults().getChannel().getLocation().getCountry();
        int resource = getResources().getIdentifier("@drawable/icon_" + info.getQuery()
                .getResults().getChannel().getItem().getCondition().getCode(), null, getPackageName());
        Drawable weatherImage = getResources().getDrawable(resource);
        loc.setText(city + ", " + country);
        conditions.setText(info.getQuery().getResults().getChannel().getItem().getCondition().getText());
        temperature.setText(info.getQuery().getResults().getChannel().getItem().getCondition().getTemp() + "\u00B0" + units);
        conditionImage.setImageDrawable(weatherImage);
    }

    @Override
    public void showProgress() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onRequestError(Throwable t) {
        Toast.makeText(this, t.toString(), Toast.LENGTH_LONG);
    }
}
