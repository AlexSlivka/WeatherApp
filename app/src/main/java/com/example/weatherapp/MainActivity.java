package com.example.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView city;
    private TextView tempNow;
    private TextView precipitationNow;
    private TextView dateNow;
    private TextView tempAtDayOfToday;
    private TextView tempAtNightOfToday;
    private TextView chanceOfRainToday;
    private TextView windToday;
    private TextView tempAtDayOfTomorrow;
    private TextView tempAtNightOfTomorrow;
    private TextView chanceOfRainTomorrow;
    private TextView windTomorrow;

    private Button update;
    private Button changeCity;

    Date currentDate = new Date();

    private int tempDefault = 0;
    private int chanceOfRainDefault = 0;
    private int windDefault = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setDate();
        setValueToView();
        setUpdateClickListener();
    }

    private void setUpdateClickListener() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDefault += 5;
                chanceOfRainDefault += 1;
                windDefault += 2;

                setValueToView();
            }
        });

    }

    public void setValueToView() {
        tempNow.setText(getString(R.string.temp_celsius, tempDefault+10));
        tempAtDayOfToday.setText(getString(R.string.temp_celsius, tempDefault+12));
        tempAtNightOfToday.setText(getString(R.string.temp_celsius, tempDefault+3));
        chanceOfRainToday.setText(getString(R.string.chance_of_rain_template, chanceOfRainDefault+6));
        windToday.setText(getString(R.string.wind_template, windDefault+2));

        tempAtDayOfTomorrow.setText(getString(R.string.temp_celsius, tempDefault+8));
        tempAtNightOfTomorrow.setText(getString(R.string.temp_celsius, tempDefault+1));
        chanceOfRainTomorrow.setText(getString(R.string.chance_of_rain_template, chanceOfRainDefault+56));
        windTomorrow.setText(getString(R.string.wind_template, windDefault+12));
    }


    private void initViews() {
        city = findViewById(R.id.city_textView);
        tempNow = findViewById(R.id.temp_now_textView);
        precipitationNow = findViewById(R.id.precipitation_now_textView);
        dateNow =findViewById(R.id.date_now_textView);
        tempAtDayOfToday = findViewById(R.id.temp_at_day_today_value);
        tempAtNightOfToday =findViewById(R.id.temp_at_night_today_value);
        chanceOfRainToday = findViewById(R.id.chance_of_rain_today_value);
        windToday = findViewById(R.id.wind_today_value);
        tempAtDayOfTomorrow = findViewById(R.id.temp_at_day_tomorrow_value);
        tempAtNightOfTomorrow = findViewById(R.id.temp_at_night_tomorrow_value);
        chanceOfRainTomorrow = findViewById(R.id.chance_of_rain_tomorrow_value);
        windTomorrow = findViewById(R.id.wind_tomorrow_value);
        update = findViewById(R.id.update_button);
        changeCity = findViewById(R.id.change_city_button);
    }

    private void setDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        dateNow.setText(dateText);
    }


}
