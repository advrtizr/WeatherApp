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
import android.widget.Toast;

import com.advrtizr.weatherservice.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.location_layout)
    RelativeLayout locLayout;
    @BindView(R.id.temp_unit_layout)
    RelativeLayout unitLayout;
    @BindView(R.id.temp_unit_value)
    TextView tempValue;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("weather_settings", MODE_PRIVATE);
        locLayout.setOnClickListener(this);
        unitLayout.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.unit_celsius:
                tempValue.setText(R.string.celsius);
                return true;
            case R.id.unit_fahrenheit:
                tempValue.setText(R.string.fahrenheit);
                return true;
            default:
                return false;

        }
    }


    //    public void saveSettings(){
//        String[] values = new String[3];
//        values[0] = etCountry.getText().toString();
//        values[1] = etCity.getText().toString();
//        values[2] = etUnits.getText().toString();
//        for(int i = 0; i < values.length; i++){
//            sharedPreferences.edit().putString(String.valueOf(i), values[i]).apply();
//        }
//    }
//
//    public void restoreSettings(){
//        etCountry.setText(sharedPreferences.getString("0", null));
//        etCity.setText(sharedPreferences.getString("1", null));
//        etUnits.setText(sharedPreferences.getString("2", null));
//    }
}
