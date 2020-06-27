package com.example.weatherapp.fragments.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.Constants;
import com.example.weatherapp.R;
import com.example.weatherapp.model.WeatherRequest;
import com.example.weatherapp.rest.OpenWeatherRepo;
import com.example.weatherapp.rest.entities.WeatherRequestRestModel;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private ProgressBar progressBar;

    private boolean visibilityWindTextView = false;
    private boolean visibilityPressureTextView = false;
    private boolean updatedDataFromServer = false;


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
        progressBar = view.findViewById(R.id.progressBar);
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
        progressBar.setVisibility(View.VISIBLE);
        OpenWeatherRepo.getInstance().getAPI().loadWeather(city + ",ru",
                "f52310dbfdea19138786c8eae8eb6138", "metric")
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            renderWeather(response.body());
                        } else {
                            //Похоже, код у нас не в диапазоне [200..300) и случилась ошибка
                            //обрабатываем ее
                            if (response.code() == 500) {
                                //ой, случился Internal Server Error. Решаем проблему
                            } else if (response.code() == 401) {
                                //не авторизованы, что-то с этим делаем.
                                //например, открываем страницу с логинкой
                            }// и так далее
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    //сбой при интернет подключении
                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        makeToast(getString(R.string.network_error));
                        progressBar.setVisibility(View.GONE);
                    }
                });
       /* UpdateFromServer updateFromServer = new UpdateFromServer(city);
        WeatherRequest weatherRequest = updateFromServer.update();
        if (weatherRequest != null) {
            DataPreparation dataPreparation = new DataPreparation(weatherRequest);
            displayWeather(dataPreparation);
        } else {
            cityTextView.setText(city);
            alertDialogMessageHome("Fail connection");
        }*/
    }

    private void renderWeather(WeatherRequestRestModel model) {
        city = model.name;
        tempNowValue = String.format(Locale.getDefault(), "%.0f", model.main.temp);
        tempAtDayOfTodayValue = String.format(Locale.getDefault(), "%.1f", model.main.tempMax);
        tempAtNightOfTodayValue = String.format(Locale.getDefault(), "%.1f", model.main.tempMin);
        windTodayValue = String.format(Locale.getDefault(), "%.1f", model.wind.speed);
        pressureTodayValue = String.format(Locale.getDefault(), "%.1f", model.main.pressure);
        recordHistory(tempNowValue);
        sendHistoryFromHome();
        setValueToView();

       /* setPlaceName(model.name, model.sys.country);
        setDetails(model.weather[0].description, model.main.humidity, model.main.pressure);
        setCurrentTemp(model.main.temp);
        setUpdatedText(model.dt);
        setWeatherIcon(model.weather[0].id,
                model.sys.sunrise * 1000,
                model.sys.sunset * 1000);*/
    }


    private void displayWeather(DataPreparation dataPreparation) {
        city = dataPreparation.getCityFromServer();
        tempNowValue = dataPreparation.getTempNowValueFromServer();
        recordHistory(tempNowValue);
        sendHistoryFromHome();
        tempAtDayOfTodayValue = dataPreparation.getTempAtDayOfTodayFromServer();
        tempAtNightOfTodayValue = dataPreparation.getTempAtNightOfTodayFromServer();
        windTodayValue = dataPreparation.getWindTodayFromServer();
        pressureTodayValue = dataPreparation.getPressureTodayFromServer();
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
        loadCityFromPreferences();
        loadHistoryFromPreferencesAfterStop();
        updateWeatherDataFromServer();
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
        if (!savedText.equals(city)) {
            city = savedText;
            updateWeatherDataFromServer();
        }
        visibilityWindTextView = sPref.getBoolean(WIND_CHECKBOX_DATA_KEY, false);
        visibilityPressureTextView = sPref.getBoolean(PRESSURE_CHECKBOX_DATA_KEY, false);
        makeToast("Get new City name");
    }

    private void alertDialogMessageHome(String message) {
        // Создаем билдер и передаем контекст приложения
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // в билдере указываем заголовок окна (можно указывать как ресурс, так и строку)
        builder.setTitle(R.string.exclamation)
                // указываем сообщение в окне (также есть вариант со строковым параметром)
                .setMessage(message)
                // можно указать и пиктограмму
                .setIcon(R.mipmap.ic_launcher_round)
                // из этого окна нельзя выйти кнопкой back
                .setCancelable(false)
                // устанавливаем кнопку (название кнопки также можно задавать строкой)
                .setPositiveButton(R.string.button,
                        // Ставим слушатель, нажатие будем обрабатывать
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "Кнопка нажата", Toast.LENGTH_SHORT).show();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

