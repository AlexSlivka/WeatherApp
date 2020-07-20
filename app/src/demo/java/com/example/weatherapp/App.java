package com.example.weatherapp;

import android.app.Application;
import androidx.room.Room;

import com.example.weatherapp.room.HistoryDao;
import com.example.weatherapp.room.HistoryDatabase;

// паттерн синглтон, наследуем класс Application
// создаем базу данных в методе onCreate
public class App extends Application {

    private static App instance;

    // база данных
    private HistoryDatabase db;

    // Так получаем объект приложения
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Это для синглтона, сохраняем объект приложения
        instance = this;

        // строим базу
        db = Room.databaseBuilder(
                getApplicationContext(),
                HistoryDatabase.class,
                "history_database")
                .allowMainThreadQueries() //Только для примеров и тестирования.
                .build();
    }

    // Получаем HistoryDao для составления запросов
    public HistoryDao getHistoryDao() {
        return db.getHistoryDao();
    }
}
