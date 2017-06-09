package com.advrtizr.weatherservice.model;

import android.util.Log;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.interfaces.LocationsRequest;
import com.advrtizr.weatherservice.interfaces.OnFilterFinishListener;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationData implements LocationModel {

    private Retrofit retrofit;

    public LocationData() {

        retrofit = new Retrofit.Builder()
                .addConverterFactory(new ConverterFactory(new Gson()))
                .baseUrl(Constants.AC_BASE_URL)
                .build();
    }

    @Override
    public void filtrate(final OnFilterFinishListener listener, String text) {

        LocationsRequest request = retrofit.create(LocationsRequest.class);
        request.getAutoComplete(text).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response != null) {
                    List<String> responseList = response.body();
                    Log.i("response", "size " + responseList.get(0));
                    listener.onResult(responseList);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    private class ConverterFactory extends Converter.Factory {

        Gson gson;

        ConverterFactory(Gson gson) {
            if (gson != null) this.gson = gson;
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new ResponseBodyConverter<>(gson, adapter);
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations,
                                                              Annotation[] methodAnnotations,
                                                              Retrofit retrofit) {
            return null;
        }
    }

    private class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        ResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            Reader reader = value.charStream();
            int item = 0;
            while (item != '(' && item != -1) {
                item = reader.read();
            }
            JsonReader jsonReader = gson.newJsonReader(reader);
            try {
                return adapter.read(jsonReader);
            } finally {
                reader.close();
            }
        }
    }
}

