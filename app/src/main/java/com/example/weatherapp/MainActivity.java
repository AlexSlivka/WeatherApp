package com.example.weatherapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String LIFECYCLE = "LIFE_CYCLE";
    public static final String TEMP_SAVE = "TEMP";
    public static final String CHANCE_OF_RAIN_SAVE = "RAIN";
    public static final String WIND_SAVE = "WIND";

//для ветки lesson03

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       initViews();
       setDate();
       setValueToView();
       setUpdateClickListener();

        if(savedInstanceState == null){
            makeToast("Первый запуск - onCreate()");
        } else {
            makeToast("Повторный запуск - onCreate()");
        }
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

    private void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(LIFECYCLE,message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeToast("onStart()");
    }

    @Override
    protected void onSaveInstanceState (Bundle saveInstanceState){
        super .onSaveInstanceState(saveInstanceState);
        makeToast("onSaveInstanceState()");
        saveInstanceState.putInt( TEMP_SAVE , tempDefault);
        saveInstanceState.putInt( CHANCE_OF_RAIN_SAVE , chanceOfRainDefault);
        saveInstanceState.putInt( WIND_SAVE , windDefault);// Сохраняем
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        makeToast("Повторный запуск - onRestoreInstanceState()");
        tempDefault = savedInstanceState.getInt(TEMP_SAVE);
        chanceOfRainDefault = savedInstanceState.getInt(CHANCE_OF_RAIN_SAVE);
        windDefault = savedInstanceState.getInt(WIND_SAVE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        makeToast("onResume()");
        setValueToView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeToast("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeToast("onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeToast("onDestroy()");
    }

}
