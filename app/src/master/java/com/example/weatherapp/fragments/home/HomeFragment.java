package com.example.weatherapp.fragments.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.App;
import com.example.weatherapp.Constants;
import com.example.weatherapp.GPSHelper;
import com.example.weatherapp.R;
import com.example.weatherapp.events.EventBus;
import com.example.weatherapp.events.StartSignInEvent;
import com.example.weatherapp.rest.OpenWeatherRepo;
import com.example.weatherapp.rest.OpenWeatherRepoCoordinates;
import com.example.weatherapp.rest.entities.WeatherRequestRestModel;
import com.example.weatherapp.room.HistoryDao;
import com.example.weatherapp.room.HistoryModel;
import com.google.android.gms.common.SignInButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;
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
    private TextView precipitationNow;

    private Button updateBtn;
    private Button myLocationBtn;
    SignInButton signInButton;

    private ImageView imageView;

    private boolean visibilityWindTextView = false;
    private boolean visibilityPressureTextView = false;
    private boolean firstStart = true;

    SharedPreferences sPref;
    SharedPreferences sPrefFirstStart;

    private String tempNowValue = "1";
    private String tempAtDayOfTodayValue = "2";
    private String tempAtNightOfTodayValue = "0";
    private String windTodayValue = "0";
    private String pressureTodayValue = "0";
    private String dateValue;
    private String latitude = "0";
    private String longitude = "0";

    private int tempDefault = 0;
    private int chanceOfRainDefault = 0;

    private HistoryDao historyDao;

    private static final String LIFECYCLE = "LIFE_CYCLE";
    private static final String TAG = "WEATHER";

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
        setMyLocationClickListener();
        setSignInClickListener();
    }

    private void setSignInClickListener() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getBus().post(new StartSignInEvent());
            }
        });
    }

    private void setMyLocationClickListener() {
        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMyLocation();
                updateWeatherDataFromServerByCoordinates();
            }
        });
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
        myLocationBtn = view.findViewById(R.id.my_location_button);
        imageView = view.findViewById(R.id.precipitation_now_imageView);
        precipitationNow = view.findViewById(R.id.precipitation_now_textView);
        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_ICON_ONLY);

        historyDao = App
                .getInstance()
                .getHistoryDao();
    }

    private void setDate() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        dateValue = df.format(c.getTime());
        dateNow.setText(dateValue);
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
                updateWeatherDataFromServer();
            }
        });
    }

    private void updateWeatherDataFromServer() {
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
                                alertDialogMessageHome("Internal Server Error");
                                //ой, случился Internal Server Error. Решаем проблему
                            } else if (response.code() == 401) {
                                alertDialogMessageHome("You are not logged in the system");
                                //не авторизованы, что-то с этим делаем.
                                //например, открываем страницу с логинкой
                            }// и так далее
                        }
                    }

                    //сбой при интернет подключении
                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        makeToast(getString(R.string.network_error));
                    }
                });
    }

    private void renderWeather(WeatherRequestRestModel model) {
        city = model.name;
        tempNowValue = String.format(Locale.getDefault(), "%.0f", model.main.temp);
        tempAtDayOfTodayValue = String.format(Locale.getDefault(), "%.1f", model.main.tempMax);
        tempAtNightOfTodayValue = String.format(Locale.getDefault(), "%.1f", model.main.tempMin);
        windTodayValue = String.format(Locale.getDefault(), "%.1f", model.wind.speed);
        pressureTodayValue = String.format(Locale.getDefault(), "%.1f", model.main.pressure);
        recordHistoryInDatabase();
        setValueToView();
        setWeatherIcon(model.weather[0].id,
                model.sys.sunrise * 1000,
                model.sys.sunset * 1000);
    }


    private void recordHistoryInDatabase() {
        HistoryModel historyModel = new HistoryModel();
        historyModel.cityNameBase = city;
        historyModel.tempNowBase = tempNowValue;
        historyModel.dateAndTimeBase = dateValue;
        historyDao.insertHistory(historyModel);
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

//Добавил google-services.json в разные варианты flavors.
    @Override
    public void onStart() {
        super.onStart();
        loadCityFromPreferences();
        setVisibilityWindTextView(visibilityWindTextView);
        setVisibilityPressureTextView(visibilityPressureTextView);
        loadFirstStartFromPreferences();
        if (firstStart){
            findMyLocation();
            updateWeatherDataFromServerByCoordinates();
            firstStart = false;
            saveFirstStartToPreferences(firstStart);
        } else {
            updateWeatherDataFromServer();
        }
    }

    void loadCityFromPreferences() {
        sPref = Objects.requireNonNull(getContext()).getSharedPreferences("CityName", MODE_PRIVATE);
        String savedText = sPref.getString(CITY_DATA_KEY, "Moscow");
        if (!savedText.equals(city)) {
            city = savedText;
            updateWeatherDataFromServer();
        }
        visibilityWindTextView = sPref.getBoolean(WIND_CHECKBOX_DATA_KEY, false);
        visibilityPressureTextView = sPref.getBoolean(PRESSURE_CHECKBOX_DATA_KEY, false);
        makeToast("Get new City name");
    }

    void loadFirstStartFromPreferences() {
        sPrefFirstStart = Objects.requireNonNull(getContext()).getSharedPreferences("FirstStart", MODE_PRIVATE);
        firstStart = sPrefFirstStart.getBoolean(FIRST_START_DATA_KEY, true);
    }

    void saveFirstStartToPreferences(boolean start) {
        sPrefFirstStart = Objects.requireNonNull(getContext()).getSharedPreferences("FirstStart", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPrefFirstStart.edit();
        ed.putBoolean(FIRST_START_DATA_KEY, start);
        ed.apply();
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

    private void loadImageWithPicasso(String iconAddress) {
        Picasso.get()
                .load(iconAddress)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = "https://images.unsplash.com/photo-1525490829609-d166ddb58678?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1049&q=80";
                precipitationNow.setText(getString(R.string.weather_sunny));
                //sunny
            } else {
                icon = "https://images.unsplash.com/photo-1517838503506-3b561768809d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1051&q=80";
                precipitationNow.setText(getString(R.string.weather_clear_night));
                //clear_night
            }
        } else {
            switch (id) {
                case 2: {
                    icon = "https://images.unsplash.com/photo-1522579579163-555c2004d810?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1053&q=80";
                    precipitationNow.setText(getString(R.string.weather_thunder));
                    //thunder
                    break;
                }
                case 3: {
                    icon = "https://images.unsplash.com/photo-1508873760731-9c3d0bb6b961?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80";
                    precipitationNow.setText(getString(R.string.weather_drizzle));
                    //drizzle
                    break;
                }
                case 5: {
                    icon = "https://images.unsplash.com/photo-1536598315365-c7bae6fd4328?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1950&q=80";
                    precipitationNow.setText(getString(R.string.weather_rainy));
                    //rainy
                    break;
                }
                case 6: {
                    icon = "https://images.unsplash.com/photo-1483143993389-85aa047c3aef?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80";
                    precipitationNow.setText(getString(R.string.weather_snowy));
                    //snowy
                    break;
                }
                case 7: {
                    icon = "https://images.unsplash.com/photo-1581184856164-0f32a960c705?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80";
                    precipitationNow.setText(getString(R.string.weather_foggy));
                    //foggy
                    break;
                }
                case 8: {
                    icon = "https://images.unsplash.com/uploads/14122598319144c6eac10/5f8e7ade?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1214&q=80";
                    precipitationNow.setText(getString(R.string.weather_cloudy));
                    //cloudy
                    break;
                }
            }
        }
        loadImageWithPicasso(icon);
    }

    private void findMyLocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            GPSHelper gpsHelper = new GPSHelper();
            Location loc = gpsHelper.getCurrentLocation(getContext());
            if (loc != null) {
                latitude = String.valueOf(loc.getLatitude());
                longitude = String.valueOf(loc.getLongitude());
                Log.d(TAG, "onCreateView: " + loc.getLatitude() + ", " + loc.getLongitude());
                makeToast("Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                makeToast("loc is null");
            }
        }
    }

    private void updateWeatherDataFromServerByCoordinates() {
        OpenWeatherRepoCoordinates.getInstance().getAPI().loadWeather(latitude, longitude,
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
                                alertDialogMessageHome("Internal Server Error");
                                //ой, случился Internal Server Error. Решаем проблему
                            } else if (response.code() == 401) {
                                alertDialogMessageHome("You are not logged in the system");
                                //не авторизованы, что-то с этим делаем.
                                //например, открываем страницу с логинкой
                            }// и так далее
                        }
                    }

                    //сбой при интернет подключении
                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        makeToast(getString(R.string.network_error));
                    }
                });
    }

}

