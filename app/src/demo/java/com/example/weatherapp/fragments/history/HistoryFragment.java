package com.example.weatherapp.fragments.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.App;
import com.example.weatherapp.Constants;
import com.example.weatherapp.R;
import com.example.weatherapp.room.HistoryDao;
import com.example.weatherapp.room.HistoryModel;

import java.util.List;

public class HistoryFragment extends Fragment implements Constants {
    private RecyclerView recyclerViewHistory;
    private String[] listData;

    private HistoryDao historyDao;

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
        loadHistoryFromDatabase();
        setupRecyclerViewHistory();
    }

    private void initViewHistory(@NonNull View view) {
        recyclerViewHistory = view.findViewById(R.id.recycler_view_history);
        historyDao = App
                .getInstance()
                .getHistoryDao();
    }

    private void setupRecyclerViewHistory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerDataAdapterHistory adapterHistory = new RecyclerDataAdapterHistory(listData);
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setAdapter(adapterHistory);
    }

    public void loadHistoryFromDatabase() {
        List<HistoryModel> historyModelList = historyDao.getAllHistory();
        listData = new String[historyModelList.size()];
        int i = 0;
        for (HistoryModel h : historyModelList) {
            listData[i] = getString(R.string.history_data_list, h.cityNameBase, h.tempNowBase, h.dateAndTimeBase);
            i++;
        }
    }

}

