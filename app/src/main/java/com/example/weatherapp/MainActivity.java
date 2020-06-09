package com.example.weatherapp;


import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    private String city = "CITY";
    private TextView cityTextView;
    private TextView tempNow;
    private TextView dateNow;
    private TextView tempAtDayOfToday;
    private TextView tempAtNightOfToday;
    private TextView chanceOfRainToday;
    private TextView windTodayTextView;
    private TextView windToday;
    private TextView pressureTodayTextView;
    private TextView pressureToday;
    private TextView tempAtDayOfTomorrow;
    private TextView tempAtNightOfTomorrow;
    private TextView chanceOfRainTomorrow;

    private Button updateBtn;
    private Button historyBtn;
    private Button changeCityBtn;

    private boolean visibilityWindTextView = false;
    private boolean visibilityPressureTextView = false;

    Date currentDate = new Date();

    private int tempDefault = 0;
    private int chanceOfRainDefault = 0;
    private int windDefault = 0;
    private int pressureDefault = 100;

    private static final String LIFECYCLE = "LIFE_CYCLE";
    public static final String TEMP_SAVE = "TEMP";
    public static final String CHANCE_OF_RAIN_SAVE = "RAIN";
    public static final String WIND_SAVE = "WIND";
    private static final String PRESSURE_SAVE = "PRESSURE";
    private int requestCodeChangeCityActivity = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setDate();
        setValueToView();
        setUpdateClickListener();
        setHistoryClickListener();
        changeCityClickListener();
        if (savedInstanceState == null) {
            makeToast("Первый запуск - onCreate()");
        } else {
            makeToast("Повторный запуск - onCreate()");
        }
    }

    private void setHistoryClickListener() {
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHistory = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intentHistory);
            }
        });
    }

    private void changeCityClickListener() {
        changeCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeCityActivity.class);
                startActivityForResult(intent, requestCodeChangeCityActivity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeChangeCityActivity) {
            if (resultCode == RESULT_OK && data != null) {
                city = data.getStringExtra(ChangeCityActivity.CITY_DATA_KEY);
                cityTextView.setText(city);
                visibilityWindTextView = data.getBooleanExtra(ChangeCityActivity.WIND_CHECKBOX_DATA_KEY, false);
                visibilityPressureTextView = data.getBooleanExtra(ChangeCityActivity.PRESSURE_CHECKBOX_DATA_KEY, false);
                setVisibilityWindTextView(visibilityWindTextView);
                setVisibilityPressureTextView(visibilityPressureTextView);
                makeToast("city changed on " + city);
                makeToast("visibilityWindTextView is " + visibilityWindTextView);
                makeToast("visibilityPressureTextView is " + visibilityPressureTextView);
            }
        }
    }

    private void setVisibilityPressureTextView(boolean visibilityPressureTextView) {
        if (visibilityPressureTextView) {
            pressureToday.setVisibility(View.VISIBLE);
            pressureTodayTextView.setVisibility(View.VISIBLE);
        } else {
            pressureToday.setVisibility(View.INVISIBLE);
            pressureTodayTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void setVisibilityWindTextView(boolean visibilityWindTextView) {
        if (visibilityWindTextView) {
            windToday.setVisibility(View.VISIBLE);
            windTodayTextView.setVisibility(View.VISIBLE);
        } else {
            windToday.setVisibility(View.GONE);
            windTodayTextView.setVisibility(View.GONE);
        }
    }

    private void setUpdateClickListener() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDefault += 5;
                chanceOfRainDefault += 1;
                windDefault += 2;
                pressureDefault += 15;
                setValueToView();
            }
        });
    }

    public void setValueToView() {
        cityTextView.setText(city);
        tempNow.setText(getString(R.string.temp_celsius, tempDefault + 10));
        tempAtDayOfToday.setText(getString(R.string.temp_celsius, tempDefault + 12));
        tempAtNightOfToday.setText(getString(R.string.temp_celsius, tempDefault + 3));
        chanceOfRainToday.setText(getString(R.string.chance_of_rain_template, chanceOfRainDefault + 6));
        windToday.setText(getString(R.string.wind_template, windDefault + 2));
        pressureToday.setText(getString(R.string.pressure_template, pressureDefault + 2));
        tempAtDayOfTomorrow.setText(getString(R.string.temp_celsius, tempDefault + 8));
        tempAtNightOfTomorrow.setText(getString(R.string.temp_celsius, tempDefault + 1));
        chanceOfRainTomorrow.setText(getString(R.string.chance_of_rain_template, chanceOfRainDefault + 56));
    }

    private void initViews() {
        cityTextView = findViewById(R.id.city_textView);
        tempNow = findViewById(R.id.temp_now_textView);
        dateNow = findViewById(R.id.date_now_textView);
        tempAtDayOfToday = findViewById(R.id.temp_at_day_today_value);
        tempAtNightOfToday = findViewById(R.id.temp_at_night_today_value);
        chanceOfRainToday = findViewById(R.id.chance_of_rain_today_value);
        windTodayTextView = findViewById(R.id.wind_today_textView);
        windToday = findViewById(R.id.wind_today_value);
        pressureTodayTextView = findViewById(R.id.pressure_today_textView);
        pressureToday = findViewById(R.id.pressure_today_value);
        tempAtDayOfTomorrow = findViewById(R.id.temp_at_day_tomorrow_value);
        tempAtNightOfTomorrow = findViewById(R.id.temp_at_night_tomorrow_value);
        chanceOfRainTomorrow = findViewById(R.id.chance_of_rain_tomorrow_value);
        updateBtn = findViewById(R.id.update_button);
        historyBtn = findViewById(R.id.history_button);
        changeCityBtn = findViewById(R.id.change_city_button);
    }

    private void setDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        dateNow.setText(dateText);
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(LIFECYCLE, message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeToast("onResume()");
        setValueToView();
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        makeToast("onSaveInstanceState()");
        saveInstanceState.putInt(TEMP_SAVE, tempDefault);
        saveInstanceState.putInt(CHANCE_OF_RAIN_SAVE, chanceOfRainDefault);
        saveInstanceState.putInt(WIND_SAVE, windDefault);
        saveInstanceState.putInt(PRESSURE_SAVE, pressureDefault);
        // Сохраняем
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        makeToast("Повторный запуск - onRestoreInstanceState()");
        tempDefault = savedInstanceState.getInt(TEMP_SAVE);
        chanceOfRainDefault = savedInstanceState.getInt(CHANCE_OF_RAIN_SAVE);
        windDefault = savedInstanceState.getInt(WIND_SAVE);
        pressureDefault = savedInstanceState.getInt(PRESSURE_SAVE);
    }
}
