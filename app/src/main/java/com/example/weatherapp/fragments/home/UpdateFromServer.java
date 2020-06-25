package com.example.weatherapp.fragments.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.model.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UpdateFromServer {
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=";
    private static final String TAG = "WEATHER";
    private static final String UPDATE = "UPDATE";


    private WeatherRequest weatherRequest;
    private String cityFromHome;

    public UpdateFromServer(String cityFromHome) {
        this.cityFromHome = cityFromHome;
    }

    public WeatherRequest getWeatherRequest() {
        return weatherRequest;
    }


    public WeatherRequest update() {
        Log.d(UPDATE, "Update begin");
        try {
            final String weather_city = String.format(WEATHER_URL, cityFromHome);
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
                        weatherRequest = gson.fromJson(resultStr, WeatherRequest.class);
                        // Возвращаемся к основному потоку
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(UPDATE, "Update end");
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                        weatherRequest = null;
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
        return weatherRequest;
    }

}
