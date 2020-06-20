package com.example.weatherapp.fragments.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Constants;
import com.example.weatherapp.EventBus;
import com.example.weatherapp.R;
import com.example.weatherapp.events.SendHistoryEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment implements Constants {
    private RecyclerView recyclerViewHistory;
    private String[] listData;

    SharedPreferences sPrefHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        initViewHistory(view);
        loadHistoryFromPreferences();
        setupRecyclerViewHistory();
    }

    private void initViewHistory(@NonNull View view) {
        recyclerViewHistory = view.findViewById(R.id.recycler_view_history);
    }

    private void setupRecyclerViewHistory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerDataAdapterHistory adapterHistory = new RecyclerDataAdapterHistory(listData);
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setAdapter(adapterHistory);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadHistoryFromPreferences();
    }

    void loadHistoryFromPreferences() {
        sPrefHistory = getContext().getSharedPreferences("History", MODE_PRIVATE);
        Set<String> ret = sPrefHistory.getStringSet(HISTORY_DATA_KEY, new HashSet<String>());
        listData = ret.toArray(new String[ret.size()]);
    }

}

