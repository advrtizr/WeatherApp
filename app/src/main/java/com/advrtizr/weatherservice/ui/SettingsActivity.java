package com.advrtizr.weatherservice.ui;

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
import com.advrtizr.weatherservice.presenter.SettingsPresenter;
import com.advrtizr.weatherservice.presenter.SettingsPresenterImpl;
import com.advrtizr.weatherservice.view.SettingsView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, SettingsView {

    @BindView(R.id.temp_unit_layout)
    RelativeLayout unitItem;
    @BindView(R.id.temp_unit_value)
    TextView tempValue;

    private final String CELSIUS = "C";
    private final String FAHRENHEIT = "F";
    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        presenter = new SettingsPresenterImpl(this);
        unitItem.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadSettings();
    }

    @Override
    public void onClick(View v) {
        animate(v);
        PopupMenu unitPopupMenu = new PopupMenu(this, v);
        unitPopupMenu.setOnMenuItemClickListener(this);
        MenuInflater unitMenu = unitPopupMenu.getMenuInflater();
        unitMenu.inflate(R.menu.unit_popup_menu, unitPopupMenu.getMenu());
        unitPopupMenu.show();

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
                presenter.saveToSettings(CELSIUS);
                presenter.loadSettings();
                return true;
            case R.id.unit_fahrenheit:
                presenter.saveToSettings(FAHRENHEIT);
                presenter.loadSettings();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void displayState(String unit) {
        tempValue.setText(unit);
    }
}
