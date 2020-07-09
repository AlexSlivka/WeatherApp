package com.example.weatherapp.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherRepoCoordinates {

    private static OpenWeatherRepoCoordinates instance = null;
    private IOpenWeatherCoordinates API;

    private OpenWeatherRepoCoordinates() {
        API = createAdapter();
    }

    public static OpenWeatherRepoCoordinates getInstance() {
        if (instance == null) {
            instance = new OpenWeatherRepoCoordinates();
        }
        return instance;
    }

    public IOpenWeatherCoordinates getAPI() {
        return API;
    }

    private IOpenWeatherCoordinates createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(IOpenWeatherCoordinates.class);
    }
}
