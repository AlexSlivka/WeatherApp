package com.example.weatherapp.rest;

import com.example.weatherapp.rest.entities.WeatherRequestRestModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherCoordinates {
    @GET("data/2.5/weather")
    Call<WeatherRequestRestModel> loadWeather(@Query("lat") String latitude,
                                              @Query("lon") String longitude,
                                              @Query("appid") String keyApi,
                                              @Query("units") String units);
}


//api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={your api key}