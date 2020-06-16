package com.example.weatherapp;


import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.model.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements Constants {

    private String city = "Moscow";
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

    private String tempNowValue = "1";
    private String tempAtDayOfTodayValue = "2";
    private String tempAtNightOfTodayValue = "0";
    private String windTodayValue = "0";
    private String pressureTodayValue = "0";

    private ArrayList<String> dataHistoryWeather
            = new ArrayList<>(Arrays.asList("Base item"));

    private int tempDefault = 0;
    private int chanceOfRainDefault = 0;
    private int windDefault = 0;
    private int pressureDefault = 100;

    private static final String LIFECYCLE = "LIFE_CYCLE";
    public static final String TEMP_SAVE = "TEMP";
    public static final String CITY_SAVE = "CITY";
    public static final String VISAB_WIND_SAVE = "VISIBILITY_WIND";
    public static final String VISAB_PRESSURE_SAVE = "VISIBILITY_PRESSURE";
    public static final String CHANCE_OF_RAIN_SAVE = "RAIN";
    public static final String WIND_SAVE = "WIND";
    private static final String PRESSURE_SAVE = "PRESSURE";
    private int requestCodeChangeCityActivity = 100;


    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=";

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
        updateWeatherDataFromServer();
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
                intentHistory.putStringArrayListExtra(DATA_HISTORY_LIST, dataHistoryWeather);
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
                String cityFromChangeCityActivity = data.getStringExtra(ChangeCityActivity.CITY_DATA_KEY);
                try {
                    if (!cityFromChangeCityActivity.equals(city)) {
                        city = cityFromChangeCityActivity;
                        updateWeatherDataFromServer();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                visibilityWindTextView = data.getBooleanExtra(ChangeCityActivity.WIND_CHECKBOX_DATA_KEY, false);
                visibilityPressureTextView = data.getBooleanExtra(ChangeCityActivity.PRESSURE_CHECKBOX_DATA_KEY, false);
                setVisibilityWindTextView(visibilityWindTextView);
                setVisibilityPressureTextView(visibilityPressureTextView);
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

    //for lesson01

    private void setUpdateClickListener() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDefault += 5;
                chanceOfRainDefault += 1;
                windDefault += 2;
                pressureDefault += 15;
                updateWeatherDataFromServer();
            }
        });
    }

    public void setValueToView() {
        cityTextView.setText(city);
        tempNow.setText(getString(R.string.temp_celsius_string, tempNowValue));
        tempAtDayOfToday.setText(getString(R.string.temp_celsius_string, tempAtDayOfTodayValue));
        tempAtNightOfToday.setText(getString(R.string.temp_celsius_string, tempAtNightOfTodayValue));
        chanceOfRainToday.setText(getString(R.string.chance_of_rain_template, chanceOfRainDefault + 6));
        windToday.setText(getString(R.string.wind_template_string, windTodayValue));
        pressureToday.setText(getString(R.string.pressure_template_string, pressureTodayValue));
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
        setVisibilityWindTextView(visibilityWindTextView);
        setVisibilityPressureTextView(visibilityPressureTextView);
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        makeToast("onSaveInstanceState()");
        saveInstanceState.putInt(TEMP_SAVE, tempDefault);
        saveInstanceState.putInt(CHANCE_OF_RAIN_SAVE, chanceOfRainDefault);
        saveInstanceState.putInt(WIND_SAVE, windDefault);
        saveInstanceState.putInt(PRESSURE_SAVE, pressureDefault);
        saveInstanceState.putString(CITY_SAVE, city);
        saveInstanceState.putBoolean(VISAB_WIND_SAVE, visibilityWindTextView);
        saveInstanceState.putBoolean(VISAB_PRESSURE_SAVE, visibilityPressureTextView);
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
        city = savedInstanceState.getString(CITY_SAVE);
        visibilityWindTextView = savedInstanceState.getBoolean(VISAB_WIND_SAVE);
        visibilityPressureTextView = savedInstanceState.getBoolean(VISAB_PRESSURE_SAVE);
    }

    private void updateWeatherDataFromServer() {
        try {
            final String weather_city = String.format(WEATHER_URL, city);
            final URL uri = new URL(weather_city + "f52310dbfdea19138786c8eae8eb6138");
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(new Runnable() {
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                        urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                        // String result = getLines(in);
                        StringBuilder result = new StringBuilder(1024);
                        String tempVariable;
                        while ((tempVariable = in.readLine()) != null) {
                            result.append(tempVariable).append("\n");
                        }
                        String resultStr = result.toString();
                        in.close();

                        // преобразование данных запроса в модель
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(resultStr, WeatherRequest.class);
                        // Возвращаемся к основному потоку
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeather(weatherRequest);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }


    private void displayWeather(WeatherRequest weatherRequest) {
        String tempNowValueForList = String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getTemp());
        if (!tempNowValueForList.equals(tempNowValue)) {
            String dataForList = getString(R.string.history_data_list, city, tempNowValueForList);
            dataHistoryWeather.add(dataForList);
        }
        tempNowValue = tempNowValueForList;
        tempAtDayOfTodayValue = String.format(Locale.getDefault(), "%.1f", weatherRequest.getMain().getTemp_max());
        tempAtNightOfTodayValue = String.format(Locale.getDefault(), "%.1f", weatherRequest.getMain().getTemp_min());
        windTodayValue = String.format(Locale.getDefault(), "%d", weatherRequest.getWind().getSpeed());
        pressureTodayValue = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getPressure());
        setValueToView();
    }

}
