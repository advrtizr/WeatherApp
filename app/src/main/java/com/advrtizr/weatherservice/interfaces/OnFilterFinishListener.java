package com.advrtizr.weatherservice.interfaces;

import java.util.List;

public interface OnFilterFinishListener {
    void onResult(List<String> filtered);

    void onFailure(Throwable t);
}
