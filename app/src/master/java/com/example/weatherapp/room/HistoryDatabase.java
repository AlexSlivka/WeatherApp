package com.example.weatherapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HistoryModel.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase{
    public abstract HistoryDao getHistoryDao();
}

