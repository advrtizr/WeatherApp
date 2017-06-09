package com.advrtizr.weatherservice.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.LocationAdapter;
import com.advrtizr.weatherservice.presenter.LocationPresenter;
import com.advrtizr.weatherservice.presenter.LocationPresenterImpl;
import com.advrtizr.weatherservice.view.LocationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends AppCompatActivity implements LocationView {

    @BindView(R.id.location_recycler)
    RecyclerView locRecycler;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    private LocationAdapter locationAdapter;
    private Snackbar snackbar;
    private Toolbar toolbar;
    private LocationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        snackbar = Snackbar.make(locRecycler, "", Snackbar.LENGTH_SHORT);
        presenter = new LocationPresenterImpl(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.showSearch(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equals("")) presenter.onTextEdited(newText);
                return true;
            }
        });
        return true;
    }
}
