package com.example.weatherapp.fragments.home;

import com.example.weatherapp.model.WeatherRequest;

import java.util.Locale;

public class DataPreparation {
    public DataPreparation(WeatherRequest weatherRequest) {
        this.weatherRequest = weatherRequest;
        setDate();
    }

    WeatherRequest weatherRequest;

    String cityFromServer;
    String tempNowValueFromServer;
    String tempAtDayOfTodayFromServer;
    String tempAtNightOfTodayFromServer;
    String windTodayFromServer;
    String pressureTodayFromServer;

    public String getCityFromServer() {
        return cityFromServer;
    }

    public String getTempNowValueFromServer() {
        return tempNowValueFromServer;
    }

    public String getTempAtDayOfTodayFromServer() {
        return tempAtDayOfTodayFromServer;
    }

    public String getTempAtNightOfTodayFromServer() {
        return tempAtNightOfTodayFromServer;
    }

    public String getWindTodayFromServer() {
        return windTodayFromServer;
    }

    public String getPressureTodayFromServer() {
        return pressureTodayFromServer;
    }


    public void setDate() {
        cityFromServer = weatherRequest.getName();
        tempNowValueFromServer = String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getTemp());
        tempAtDayOfTodayFromServer = String.format(Locale.getDefault(), "%.1f", weatherRequest.getMain().getTemp_max());
        tempAtNightOfTodayFromServer = String.format(Locale.getDefault(), "%.1f", weatherRequest.getMain().getTemp_min());
        windTodayFromServer = String.format(Locale.getDefault(), "%d", weatherRequest.getWind().getSpeed());
        pressureTodayFromServer = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getPressure());
    }
}
