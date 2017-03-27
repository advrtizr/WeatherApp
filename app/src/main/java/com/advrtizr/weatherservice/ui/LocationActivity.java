package com.advrtizr.weatherservice.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.LocationAdapter;
import com.advrtizr.weatherservice.presenter.LocationPresenter;
import com.advrtizr.weatherservice.presenter.LocationPresenterImpl;
import com.advrtizr.weatherservice.view.LocationView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends AppCompatActivity implements LocationView {

    @BindView(R.id.location_recycler)
    RecyclerView locRecycler;
    @BindView(R.id.et_filter)
    EditText filter;

    private LocationAdapter locationAdapter;
    private Snackbar snackbar;
    private Toolbar toolbar;
    private LocationPresenter presenter;

    public static final String LOCATION_PREF = "location_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        snackbar = Snackbar.make(locRecycler, "", Snackbar.LENGTH_SHORT);
        presenter = new LocationPresenterImpl(this, filter);
        presenter.onTextEdited();
        locRecycler.setLayoutManager(new LinearLayoutManager(this));
        locRecycler.setClickable(true);
    }

    @Override
    public void displayLocation(List<String> filtered) {
        locationAdapter = new LocationAdapter(this, filtered);
        locRecycler.setAdapter(locationAdapter);
    }

    @Override
    public void onRequestSuccess() {

    }

    @Override
    public void onRequestError(Throwable t) {
        snackbar.setText(R.string.snackbar_connection_error);
        snackbar.show();
    }
}
