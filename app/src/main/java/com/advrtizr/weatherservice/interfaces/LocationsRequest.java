package com.advrtizr.weatherservice.interfaces;

import com.advrtizr.weatherservice.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationsRequest {
    @GET(Constants.AC_GET_URI)
    Call<List<String>> getAutoComplete(@Query("q") String text);
}
