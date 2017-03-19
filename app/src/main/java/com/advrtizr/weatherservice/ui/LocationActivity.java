package com.advrtizr.weatherservice.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.LocationAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

public class LocationActivity extends AppCompatActivity {

    @BindView(R.id.location_recycler)
    RecyclerView locRecycler;

    private List<String> locList;
    private LocationAdapter locationAdapter;

    public static final String LOCATION_PREF = "location_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        locList = loadFile();
        locationAdapter = new LocationAdapter(this, locList);
        locRecycler.setLayoutManager(new LinearLayoutManager(this));
        locRecycler.setClickable(true);
        locRecycler.setAdapter(locationAdapter);
    }

    public ArrayList<String> loadFile(){
        ArrayList<String> lines = new ArrayList<>();
        try{
            InputStream inputStream = getResources().openRawResource(R.raw.location);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null){
                lines.add(line);
            }
            inputStream.close();
        }
        catch(java.io.IOException e){
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location_clear:
                SharedPreferences sp = getSharedPreferences(LOCATION_PREF, MODE_PRIVATE);
                sp.edit().clear().apply();
                locationAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
