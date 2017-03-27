package com.advrtizr.weatherservice.view;

import java.util.List;

public interface LocationView {
    void displayLocation(List<String> filtered);
    void onRequestSuccess();
    void onRequestError(Throwable t);
}
