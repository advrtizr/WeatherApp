package com.advrtizr.weatherservice.presenter;

import com.advrtizr.weatherservice.interfaces.OnFilterFinishListener;
import com.advrtizr.weatherservice.model.LocationData;
import com.advrtizr.weatherservice.model.LocationModel;
import com.advrtizr.weatherservice.view.LocationView;
import java.util.List;

public class LocationPresenterImpl implements LocationPresenter, OnFilterFinishListener {

    private LocationView locationView;
    private LocationModel locationData;

    public LocationPresenterImpl(LocationView locationView) {
        this.locationView = locationView;
        locationData = new LocationData();
    }

    @Override
    public void onTextEdited(String searchable) {
        if(searchable.isEmpty()){

        }
        if(searchable.length() >= 3)
        locationData.filtrate(LocationPresenterImpl.this, searchable);
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
