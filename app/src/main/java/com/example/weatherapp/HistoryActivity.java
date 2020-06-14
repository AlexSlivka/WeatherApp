package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements Constants{
    private RecyclerView recyclerViewHistory;
    private String[] listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initViewHistory();
        ArrayList<String> listDataFromMain = getIntent().getStringArrayListExtra(DATA_HISTORY_LIST);
      //  Toast.makeText(getApplicationContext(), listDataFromMain, Toast.LENGTH_SHORT).show();
       listData = new String[listDataFromMain.size()];
       listDataFromMain.toArray(listData);
        setupRecyclerViewHistory();
    }

    private void setupRecyclerViewHistory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
       // listData = getResources().getStringArray(R.array.history_days_array);
        RecyclerDataAdapterHistory adapterHistory = new RecyclerDataAdapterHistory(listData);
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setAdapter(adapterHistory);
    }

    private void initViewHistory() {
        recyclerViewHistory = findViewById(R.id.recycler_view_history);
    }
}