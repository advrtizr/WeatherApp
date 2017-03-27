package com.advrtizr.weatherservice;

/**
 * Created by Камикадзе on 30.01.2017.
 */

public class Constants {

    public static final String BASE_URL = "https://query.yahooapis.com";
    public static final String AC_BASE_URL = "http://gd.geobytes.com/";
    public static final String GET_URL = "/v1/public/yql";
    public static final String LOCATION_PART = "/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text=\"";
    public static final String UNIT_PART = "\")%20and%20u='";
    public static final String END_PART = "'&format=json";
}
