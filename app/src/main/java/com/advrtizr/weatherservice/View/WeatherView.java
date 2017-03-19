package com.advrtizr.weatherservice.view;

public interface WeatherView {

    void showProgress();
    void hideProgress();
    void onRequestSuccess();
    void onRequestError(Throwable t);
}
