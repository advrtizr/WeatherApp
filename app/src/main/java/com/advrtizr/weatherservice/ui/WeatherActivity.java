package com.advrtizr.weatherservice.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.WeatherAdapter;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;
import com.advrtizr.weatherservice.presenter.WeatherPresenter;
import com.advrtizr.weatherservice.presenter.WeatherPresenterImpl;
import com.advrtizr.weatherservice.view.WeatherView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherView, View.OnClickListener {

    @BindView(R.id.card_container)
    RecyclerView recyclerView;
    @BindView(R.id.activity_weather)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout refreshLayout;

    private WeatherAdapter weatherAdapter;
    private Snackbar snackbar;
    private WeatherPresenter presenter;
    private ItemTouchHelper touchHelper;
    private SharedPreferences locationPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        snackbar = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_SHORT);

        locationPref = getSharedPreferences(LocationActivity.LOCATION_PREF, MODE_PRIVATE);
        touchHelper = new ItemTouchHelper(callbackHelper());
        touchHelper.attachToRecyclerView(recyclerView);

        initFab();
        initRefresh();
        initPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ItemTouchHelper.Callback callbackHelper() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                presenter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                weatherAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//                weatherAdapter.notifyItemRangeRemoved(0, previousContentSize);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                presenter.deleteItem(viewHolder.getAdapterPosition());
                weatherAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
    }

    private void initAdapter(List<WeatherInfo> list){
        weatherAdapter = new WeatherAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(weatherAdapter);
    }


    private void initPresenter() {
        presenter = new WeatherPresenterImpl(WeatherActivity.this, locationPref);
        presenter.requestWeather();
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(locationPref.getAll().size() != 0){
                    initPresenter();
                }else{
                    refreshLayout.setRefreshing(false);
                    snackbar.setText(R.string.warning).show();
                }
            }
        });
        refreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initFab() {
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

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRequestSuccess(List<WeatherInfo> list) {
        initAdapter(list);
        snackbar.setText(R.string.snackbar_refreshed);
        snackbar.show();
    }

    @Override
    public void onRequestError(List<WeatherInfo> list) {
        initAdapter(list);
        snackbar.setText(R.string.snackbar_connection_error);
        refreshLayout.setRefreshing(false);
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
