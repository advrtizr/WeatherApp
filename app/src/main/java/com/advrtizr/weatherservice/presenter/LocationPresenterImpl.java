package com.advrtizr.weatherservice.presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.advrtizr.weatherservice.interfaces.OnFilterFinishListener;
import com.advrtizr.weatherservice.model.LocationData;
import com.advrtizr.weatherservice.model.LocationModel;
import com.advrtizr.weatherservice.view.LocationView;

import java.util.List;

public class LocationPresenterImpl implements LocationPresenter, OnFilterFinishListener {

    private LocationView locationView;
    private LocationModel locationData;
    private EditText filter;

    public LocationPresenterImpl(LocationView locationView, EditText filter) {
        this.locationView = locationView;
        this.filter = filter;
        locationData = new LocationData();
    }

    @Override
    public void onTextEdited() {
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null){
                    String text = s.toString();
                    locationData.filtrate(LocationPresenterImpl.this, text);
                }
            }

        });
    }

    @Override
    public void onResult(List<String> filtered) {
        locationView.displayLocation(filtered);
        locationView.onRequestSuccess();
    }

    @Override
    public void onFailure(Throwable t) {
        locationView.onRequestError(t);
    }
}
