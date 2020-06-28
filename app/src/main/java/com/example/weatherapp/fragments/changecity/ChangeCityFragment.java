package com.example.weatherapp.fragments.changecity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Constants;
import com.example.weatherapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class ChangeCityFragment extends Fragment implements OnItemCitiesClick, Constants {
    private String cityChange = "";
    private TextInputEditText cityEnterEditText;
    private CheckBox windSpeedChBox;
    private CheckBox pressureChBox;
    private Button confirmSelectionBtn;

    private RecyclerView recyclerViewCities;
    private String[] listDataCities;

    SharedPreferences sPref;

    Pattern checkCityEnterEditTextPattern = Pattern.compile("^[A-Z][a-z]{2,}\\s*[A-Z]*[a-z]*$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        initViewsChangeCityActivity(view);
        setupRecyclerViewCities();
        setClickListenerConfirmSelectionBtn();
        checkCityEnterEditTextField();
    }

    private void initViewsChangeCityActivity(@NonNull View view) {
        cityEnterEditText = view.findViewById(R.id.enter_city_editText);
        windSpeedChBox = view.findViewById(R.id.wind_speed_checkBox);
        pressureChBox = view.findViewById(R.id.pressure_checkBox);
        confirmSelectionBtn = view.findViewById(R.id.confirm_selection_button);
        recyclerViewCities = view.findViewById(R.id.recycler_view_history);
    }

    private void setupRecyclerViewCities() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        listDataCities = getResources().getStringArray(R.array.cities_array);
        RecyclerDataAdapterCities adapterCities = new RecyclerDataAdapterCities(listDataCities, this);
        recyclerViewCities.setLayoutManager(layoutManager);
        recyclerViewCities.setAdapter(adapterCities);
    }

    private void setClickListenerConfirmSelectionBtn() {
        confirmSelectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityChange = cityEnterEditText.getText().toString();
                if (validateOnConfirmSelection(cityEnterEditText, checkCityEnterEditTextPattern, "Invalid city name")) {
                    saveCityToPreferences(cityChange);
                    Snackbar.make(cityEnterEditText, "City changed", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    alertDialogMessage("Check entered data");
                }
            }
        });
    }

    private void alertDialogMessage(String message) {
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

    void saveCityToPreferences(String name) {
        sPref = Objects.requireNonNull(getContext()).getSharedPreferences("CityName", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(CITY_DATA_KEY, name);
        ed.putBoolean(WIND_CHECKBOX_DATA_KEY, windSpeedChBox.isChecked());
        ed.putBoolean(PRESSURE_CHECKBOX_DATA_KEY, pressureChBox.isChecked());
        ed.apply();
        Toast.makeText(getContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();

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

    private void checkCityEnterEditTextField() {
        // Проверка ввода названия города при потере фокуса
        cityEnterEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                validate(tv, checkCityEnterEditTextPattern, "Invalid city name");
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

    // Показать ошибку
    private void showError(TextView view, String message) {
        view.setError(message);
    }

    // спрятать ошибку
    private void hideError(TextView view) {
        view.setError(null);
    }

    @Override
    public void onItemCitiesClicked(String itemText) {
        Toast.makeText(getContext(), itemText, Toast.LENGTH_SHORT).show();
        cityEnterEditText.setText(itemText);
    }

}