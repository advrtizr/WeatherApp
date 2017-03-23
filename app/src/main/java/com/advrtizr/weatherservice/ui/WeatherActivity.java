package com.advrtizr.weatherservice.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.WeatherAdapter;
import com.advrtizr.weatherservice.presenter.WeatherPresenter;
import com.advrtizr.weatherservice.presenter.WeatherPresenterImpl;
import com.advrtizr.weatherservice.view.WeatherView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherView, View.OnClickListener {


    @BindView(R.id.weather_card_container)
    RecyclerView recyclerView;
    @BindView(R.id.activity_weather)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private WeatherAdapter weatherAdapter;
    private Map<String, ?> locationList;
    private List<WeatherPresenter> presenters;
    private SharedPreferences locationPreferences;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        snackbar = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_SHORT);
        initializeFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeAdapter();
    }

    private void initializeFab(){
        fabAdd.setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fabAdd.isShown())
                    fabAdd.hide();
                if (dy < 0 && fabAdd.isShown())
                    fabAdd.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fabAdd.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void initializeAdapter() {
        presenters = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        locationPreferences = getSharedPreferences(LocationActivity.LOCATION_PREF, MODE_PRIVATE);
        locationList = locationPreferences.getAll();
        for (Map.Entry location : locationList.entrySet()) {
            if (location == null) {
                return;
            }
            String key = location.getKey().toString();
            String city = location.getValue().toString();
            keys.add(key);
            presenters.add(new WeatherPresenterImpl(this, this, city));
        }
        weatherAdapter = new WeatherAdapter(this, keys, presenters, locationPreferences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(weatherAdapter);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onRequestSuccess() {
        weatherAdapter.notifyDataSetChanged();
        snackbar.setText(R.string.snackbar_refreshed);
        snackbar.show();
    }

    @Override
    public void onRequestError(Throwable t) {
        snackbar.setText(R.string.snackbar_connection_error);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent toLocation = new Intent(this, LocationActivity.class);
        startActivity(toLocation);
    }
}
