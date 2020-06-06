package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerViewHistory;
    private String[] listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initViewHistory();
        setupRecyclerViewHistory();
    }

    private void setupRecyclerViewHistory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        listData = getResources().getStringArray(R.array.history_days_array);
        RecyclerDataAdapterHistory adapterHistory = new RecyclerDataAdapterHistory(listData);
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setAdapter(adapterHistory);
    }

    private void initViewHistory() {
        recyclerViewHistory = findViewById(R.id.recycler_view_history);
    }
}