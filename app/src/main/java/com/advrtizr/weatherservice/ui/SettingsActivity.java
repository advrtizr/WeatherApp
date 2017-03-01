package com.advrtizr.weatherservice.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.LocationAdapter;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.location_layout)
    RelativeLayout locItem;
    @BindView(R.id.temp_unit_layout)
    RelativeLayout unitItem;
    @BindView(R.id.temp_unit_value)
    TextView tempValue;
    @BindView(R.id.location_value)
    TextView locValue;

    private SharedPreferences sharedPreferences;

    public static final String SETTINGS_PREF = "settings_pref";
    public static final String UNIT = "unit";
    public static final String LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        locItem.setOnClickListener(this);
        unitItem.setOnClickListener(this);
        tempValue.setText(restoreUnits(UNIT));
    }


    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(LocationActivity.LOCATION_PREF, MODE_PRIVATE);
        locValue.setText(setLocation(sharedPreferences));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_layout:
                animate(v);
                Intent toLocation = new Intent(this, LocationActivity.class);
                startActivity(toLocation);
                break;
            case R.id.temp_unit_layout:
                animate(v);
                PopupMenu unitPopupMenu = new PopupMenu(this, v);
                unitPopupMenu.setOnMenuItemClickListener(this);
                MenuInflater unitMenu = unitPopupMenu.getMenuInflater();
                unitMenu.inflate(R.menu.unit_popup_menu, unitPopupMenu.getMenu());
                unitPopupMenu.setGravity(Gravity.CENTER_VERTICAL);
                Log.i("gravity", String.valueOf(unitPopupMenu.getGravity()));
                unitPopupMenu.show();
                break;
        }
    }

    public void animate(View v) {
        Animation scale = new AlphaAnimation(0.1f, 1.0f);
        scale.setDuration(500);
        v.startAnimation(scale);
    }

    public String setLocation(SharedPreferences sPref){
        String location = "";
        Map<String, ?> locationMap = sPref.getAll();
        for(Map.Entry<String, ?> entry : locationMap.entrySet()){
            if(entry != null){
                String key = entry.getKey();
                location = sPref.getString(key, null);
            }
        }
        return location;
    }

    public void saveUnits(int resource, TextView view, String key) {
        view.setText(resource);

        sharedPreferences = getSharedPreferences(SETTINGS_PREF, MODE_PRIVATE);
        if(resource != 0){
            sharedPreferences.edit().putInt(key, resource).apply();
        }
    }

    public int restoreUnits(String key) {
        int defaultValue = R.id.unit_celsius;
        sharedPreferences = getSharedPreferences(SETTINGS_PREF, MODE_PRIVATE);
        int prefEntry = sharedPreferences.getInt(key, 0);

        if(prefEntry != 0){
            return prefEntry;
        }
        return defaultValue;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.unit_celsius:
                saveUnits(R.string.celsius, tempValue, UNIT);
                return true;
            case R.id.unit_fahrenheit:
                saveUnits(R.string.fahrenheit, tempValue, UNIT);
                return true;
            default:
                return false;
        }
    }
}
