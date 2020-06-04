package com.example.weatherapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ChangeCityActivity extends AppCompatActivity {
    private String cityChange = " ";
    private TextView cityEnterEditText;
    private CheckBox windSpeedChBox;
    private CheckBox pressureChBox;
    private Button cancelBtn;
    private Button confirmSelectionBtn;

    public final static String CITY_DATA_KEY = "CITYDATAKEY";
    public final static String WIND_CHECKBOX_DATA_KEY = "WINDDATAKEY";
    public final static String PRESSURE_CHECKBOX_DATA_KEY = "PRESSUREDATAKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        initViewsChangeCityActivity();
        setClickListenerConfirmSelectionBtn();
        setClickListenerCancelBtn();
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
    }
}
