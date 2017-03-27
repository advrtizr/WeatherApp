package com.advrtizr.weatherservice.model;

import com.advrtizr.weatherservice.interfaces.OnFilterFinishListener;

public interface LocationModel {
    void filtrate(OnFilterFinishListener listener, String text);
}
