package com.example.weatherapp.fragments.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.Constants;
import com.example.weatherapp.EventBus;
import com.example.weatherapp.R;
import com.example.weatherapp.events.SendHistoryEvent;
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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements Constants {
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

    private boolean visibilityWindTextView = false;
    private boolean visibilityPressureTextView = false;

    Date currentDate = new Date();

    SharedPreferences sPref;
    SharedPreferences sPrefHistory;

    private String tempNowValue = "1";
    private String tempAtDayOfTodayValue = "2";
    private String tempAtNightOfTodayValue = "0";
    private String windTodayValue = "0";
    private String pressureTodayValue = "0";

    private Set<String> dataHistoryWeathers;

    private int tempDefault = 0;
    private int chanceOfRainDefault = 0;
    private int windDefault = 0;
    private int pressureDefault = 100;

    private static final String LIFECYCLE = "LIFE_CYCLE";
    private int requestCodeChangeCityActivity = 100;


    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        initViews(view);
        setDate();
        setValueToView();
        setUpdateClickListener();
        dataHistoryWeathers = new LinkedHashSet<>();
        updateWeatherDataFromServer();
    }


    private void initViews(@NonNull View view) {
        cityTextView = view.findViewById(R.id.city_textView);
        tempNow = view.findViewById(R.id.temp_now_textView);
        dateNow = view.findViewById(R.id.date_now_textView);
        tempAtDayOfToday = view.findViewById(R.id.temp_at_day_today_value);
        tempAtNightOfToday = view.findViewById(R.id.temp_at_night_today_value);
        chanceOfRainToday = view.findViewById(R.id.chance_of_rain_today_value);
        windTodayTextView = view.findViewById(R.id.wind_today_textView);
        windToday = view.findViewById(R.id.wind_today_value);
        pressureTodayTextView = view.findViewById(R.id.pressure_today_textView);
        pressureToday = view.findViewById(R.id.pressure_today_value);
        tempAtDayOfTomorrow = view.findViewById(R.id.temp_at_day_tomorrow_value);
        tempAtNightOfTomorrow = view.findViewById(R.id.temp_at_night_tomorrow_value);
        chanceOfRainTomorrow = view.findViewById(R.id.chance_of_rain_tomorrow_value);
        updateBtn = view.findViewById(R.id.update_button);
    }

    private void setDate() {
        //  DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", getContext());//????
        //  String dateText = dateFormat.format(currentDate);
        String dateText = "Now";
        dateNow.setText(dateText);
    }

    private void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(LIFECYCLE, message);
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
        recordHistory(tempNowValueForList);
        sendHistoryFromHome();
        tempAtDayOfTodayValue = String.format(Locale.getDefault(), "%.1f", weatherRequest.getMain().getTemp_max());
        tempAtNightOfTodayValue = String.format(Locale.getDefault(), "%.1f", weatherRequest.getMain().getTemp_min());
        windTodayValue = String.format(Locale.getDefault(), "%d", weatherRequest.getWind().getSpeed());
        pressureTodayValue = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getPressure());
        setValueToView();
    }

    private void recordHistory(String tempNowValueForList) {
        String dataForList = getString(R.string.history_data_list, city, tempNowValueForList);
        dataHistoryWeathers.add(dataForList);
        tempNowValue = tempNowValueForList;
    }

    private void sendHistoryFromHome() {
        sPrefHistory = getContext().getSharedPreferences("History", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPrefHistory.edit();
        ed.putStringSet(HISTORY_DATA_KEY, dataHistoryWeathers);
        ed.commit();
    }

    void loadHistoryFromPreferencesAfterStop() {
        sPrefHistory = getContext().getSharedPreferences("History", MODE_PRIVATE);
        dataHistoryWeathers = sPrefHistory.getStringSet(HISTORY_DATA_KEY, new LinkedHashSet<String>());
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        loadCityFromPreferences();
        loadHistoryFromPreferencesAfterStop();
        setVisibilityWindTextView(visibilityWindTextView);
        setVisibilityPressureTextView(visibilityPressureTextView);
    }

    @Override
    public void onStop() {
        sendHistoryFromHome();
        super.onStop();
    }

    void loadCityFromPreferences() {
        sPref = getContext().getSharedPreferences("CityName", MODE_PRIVATE);
        String savedText = sPref.getString(CITY_DATA_KEY, "");
        if (!savedText.equals(city) || !savedText.equals("")) {
            city = savedText;
            updateWeatherDataFromServer();
        }
        visibilityWindTextView = sPref.getBoolean(WIND_CHECKBOX_DATA_KEY, false);
        visibilityPressureTextView = sPref.getBoolean(PRESSURE_CHECKBOX_DATA_KEY, false);
        makeToast("Get new City name");

    }

}

