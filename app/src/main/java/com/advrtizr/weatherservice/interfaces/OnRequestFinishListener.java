package com.advrtizr.weatherservice.interfaces;

public interface OnRequestFinishListener {
    void onResult(Object obj);

    void onFailure(Throwable t);
}
