package com.example.weatherapp;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class ChangeCityActivity extends AppCompatActivity implements OnItemCitiesClick {
    private String cityChange = " ";
    private TextInputEditText cityEnterEditText;
    private CheckBox windSpeedChBox;
    private CheckBox pressureChBox;
    private Button cancelBtn;
    private Button confirmSelectionBtn;

    private RecyclerView recyclerViewCities;
    private String[] listDataCities;

    public final static String CITY_DATA_KEY = "CITYDATAKEY";
    public final static String WIND_CHECKBOX_DATA_KEY = "WINDDATAKEY";
    public final static String PRESSURE_CHECKBOX_DATA_KEY = "PRESSUREDATAKEY";

    Pattern checkCityEnterEditText = Pattern.compile("^[A-Z][a-z]{2,}\\s*[A-Z]*[a-z]*$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        initViewsChangeCityActivity();
        setupRecyclerViewCities();
        setClickListenerConfirmSelectionBtn();
        setClickListenerCancelBtn();
        checkCityEnterEditTextField();
    }

    private void checkCityEnterEditTextField() {
        // Проверка ввода названия города при потере фокуса
        cityEnterEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                validate(tv, checkCityEnterEditText, "Invalid city name");
            }
        });
    }


    // Валидация
    private void validate(TextView tv, Pattern check, String message) {
        String value = tv.getText().toString();
        if (check.matcher(value).matches()) {    // Проверим на основе регулярных выражений
            hideError(tv);
        } else {
            showError(tv, message);
        }
    }

    private boolean validateOnConfirmSelection(TextView tv, Pattern check, String message) {
        Boolean result = true;
        String value = tv.getText().toString();
        if (check.matcher(value).matches()) {    // Проверим на основе регулярных выражений
            hideError(tv);
        } else {
            showError(tv, message);
            result = false;
        }
        return result;
    }


    // Показать ошибку
    private void showError(TextView view, String message) {
        view.setError(message);
    }

    // спрятать ошибку
    private void hideError(TextView view) {
        view.setError(null);
    }

    private void setupRecyclerViewCities() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        listDataCities = getResources().getStringArray(R.array.cities_array);
        RecyclerDataAdapterCities adapterCities = new RecyclerDataAdapterCities(listDataCities, this);
        recyclerViewCities.setLayoutManager(layoutManager);
        recyclerViewCities.setAdapter(adapterCities);
    }

    private void setClickListenerCancelBtn() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setClickListenerConfirmSelectionBtn() {
        confirmSelectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityChange = cityEnterEditText.getText().toString();
                if (validateOnConfirmSelection(cityEnterEditText, checkCityEnterEditText, "Invalid city name")) {
                    Intent cityDataIntent = new Intent();
                    cityDataIntent.putExtra(CITY_DATA_KEY, cityChange);
                    cityDataIntent.putExtra(WIND_CHECKBOX_DATA_KEY, windSpeedChBox.isChecked());
                    cityDataIntent.putExtra(PRESSURE_CHECKBOX_DATA_KEY, pressureChBox.isChecked());
                    setResult(RESULT_OK, cityDataIntent);
                    Snackbar.make(cityEnterEditText, "City changed", Snackbar.LENGTH_LONG)
                            .show();
                    finish();
                } else {
                    Snackbar.make(cityEnterEditText, "Check entered data", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void initViewsChangeCityActivity() {
        cityEnterEditText = findViewById(R.id.enter_city_editText);
        windSpeedChBox = findViewById(R.id.wind_speed_checkBox);
        pressureChBox = findViewById(R.id.pressure_checkBox);
        cancelBtn = findViewById(R.id.cancel_button);
        confirmSelectionBtn = findViewById(R.id.confirm_selection_button);
        recyclerViewCities = findViewById(R.id.recycler_view_history);
    }

    @Override
    public void onItemCitiesClicked(String itemText) {
        Toast.makeText(getApplication(), itemText, Toast.LENGTH_SHORT).show();
        cityEnterEditText.setText(itemText);
    }
}
//for lesson06