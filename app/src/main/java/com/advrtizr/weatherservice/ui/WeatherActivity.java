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
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.interfaces.ItemTouchHelperViewHolder;
import com.advrtizr.weatherservice.interfaces.OnListChangeListener;
import com.advrtizr.weatherservice.interfaces.OnStartDragListener;
import com.advrtizr.weatherservice.model.WeatherAdapter;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;
import com.advrtizr.weatherservice.presenter.WeatherPresenter;
import com.advrtizr.weatherservice.presenter.WeatherPresenterImpl;
import com.advrtizr.weatherservice.view.WeatherView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherView, View.OnClickListener, OnListChangeListener, OnStartDragListener{

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
    private List<WeatherInfo> weather = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_settings:
                        Intent intent = new Intent(WeatherActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
        snackbar = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_SHORT);
        locationPref = getSharedPreferences(LocationActivity.LOCATION_PREF, MODE_PRIVATE);

        initFab();
        initRefresh();
        initPresenter();
        initAdapter(weather);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initAdapter(List<WeatherInfo> list) {
        if(list == null) return;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        weatherAdapter = new WeatherAdapter(this, locationPref, list, this, this);
        touchHelper = new ItemTouchHelper(callback());
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(weatherAdapter);
    }

    private void initPresenter() {
        presenter = new WeatherPresenterImpl(WeatherActivity.this, locationPref);
        presenter.requestWeather();
        List<WeatherInfo> temp = presenter.loadWeather();
        if(temp != null){
            weather = temp;
        }
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (locationPref.getAll().size() != 0) {
                    initPresenter();
                } else {
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

    private ItemTouchHelper.Callback callback() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            private Integer mFrom = null;
            private Integer mTo = null;

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                if (source.getItemViewType() != target.getItemViewType()) {
                    return false;
                }

                if (mFrom == null) mFrom = source.getAdapterPosition();
                mTo = target.getAdapterPosition();

                weatherAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                weatherAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                    itemViewHolder.onItemClear();
                }

                if (mFrom != null && mTo != null)
                    weatherAdapter.onItemDrop(mFrom, mTo);

                mFrom = mTo = null;
            }
        };
        return callback;
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
//        weather = list;
        initAdapter(list);
//        weatherAdapter.notifyDataSetChanged();
        Log.i("adapter", "list size " + String.valueOf(list.size()));
        snackbar.setText(R.string.snackbar_refreshed);
        snackbar.show();
    }

    @Override
    public void onRequestError(List<WeatherInfo> list) {
        snackbar.setText(R.string.snackbar_connection_error);
        refreshLayout.setRefreshing(false);
        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        Intent toLocation = new Intent(this, LocationActivity.class);
        startActivity(toLocation);
    }

    @Override
    public void onListChanged(List<WeatherInfo> list) {
        if(list != null) presenter.saveData(list);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
}
