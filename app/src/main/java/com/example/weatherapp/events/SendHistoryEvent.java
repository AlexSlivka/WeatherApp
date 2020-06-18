package com.example.weatherapp.events;

import java.util.ArrayList;
import java.util.Arrays;

public class SendHistoryEvent {
    private ArrayList<String> dataHistoryWeatherEvents;


    public ArrayList<String> getDataHistoryWeatherEvents() {
        return dataHistoryWeatherEvents;
    }

    public void setDataHistoryWeatherEvents(ArrayList<String> dataHistoryWeatherEvents) {
        this.dataHistoryWeatherEvents = dataHistoryWeatherEvents;
    }

    public SendHistoryEvent(ArrayList<String> dataHistoryWeatherEvents) {
        this.dataHistoryWeatherEvents = dataHistoryWeatherEvents;
    }

}
