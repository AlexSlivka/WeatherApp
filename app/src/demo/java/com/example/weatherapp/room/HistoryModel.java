package com.example.weatherapp.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity - это признак табличного объекта, то есть объект
// будет сохранятся в базе данных в виде строки
// indices - указывает на индексы в таблице
@Entity
public class HistoryModel {
    public final static String ID = "id";
    public final static String CITY_NAME = "city_name";
    public final static String TEMP_NOW = "temperature";
    public final static String DATE_AND_TIME = "date_and_time";

    // @PrimaryKey - указывает на ключевую запись,
    // autoGenerate = true - автоматическая генерация ключа
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long id;

    // название города
    // @ColumnInfo - позволяет задавать параметры колонки в БД
    // name = "city_name" - такое будет имя колонки
    @ColumnInfo(name = CITY_NAME)
    public String cityNameBase;

    // температрура сейчас
    // @ColumnInfo - позволяет задавать параметры колонки в БД
    // name = "temperature" - такое будет имя колонки
    @ColumnInfo(name = TEMP_NOW)
    public String tempNowBase;

    // дата и время
    // @ColumnInfo - позволяет задавать параметры колонки в БД
    // name = "date_and_time" - такое будет имя колонки
    @ColumnInfo(name = DATE_AND_TIME)
    public String dateAndTimeBase;
}
