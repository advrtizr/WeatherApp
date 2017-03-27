package com.advrtizr.weatherservice.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationsRequest {
    @GET("AutoCompleteCity?callback=?&")
    Call<List<String>> getAutoComplete(@Query("q") String text);
}
