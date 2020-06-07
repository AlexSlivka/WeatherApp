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

public class ChangeCityActivity extends AppCompatActivity implements OnItemCitiesClick {
    private String cityChange = " ";
    private TextView cityEnterEditText;
    private CheckBox windSpeedChBox;
    private CheckBox pressureChBox;
    private Button cancelBtn;
    private Button confirmSelectionBtn;

    private RecyclerView recyclerViewCities;
    private String[] listDataCities;

    public final static String CITY_DATA_KEY = "CITYDATAKEY";
    public final static String WIND_CHECKBOX_DATA_KEY = "WINDDATAKEY";
    public final static String PRESSURE_CHECKBOX_DATA_KEY = "PRESSUREDATAKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        initViewsChangeCityActivity();
        setupRecyclerViewCities();
        setClickListenerConfirmSelectionBtn();
        setClickListenerCancelBtn();
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
                Intent cityDataIntent = new Intent();
                cityDataIntent.putExtra(CITY_DATA_KEY, cityChange);
                cityDataIntent.putExtra(WIND_CHECKBOX_DATA_KEY, windSpeedChBox.isChecked());
                cityDataIntent.putExtra(PRESSURE_CHECKBOX_DATA_KEY, pressureChBox.isChecked());
                setResult(RESULT_OK, cityDataIntent);
                finish();
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
