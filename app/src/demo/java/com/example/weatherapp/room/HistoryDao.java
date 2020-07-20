package com.example.weatherapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// Описание объекта, обрабатывающего данные
// @Dao - доступ к данным
// В этом классе описывается, как будет происходить обработка данных
@Dao
public interface HistoryDao {

    // Метод для добавления студента в базу данных
    // @Insert - признак добавления
    // onConflict - что делать, если такая запись уже есть
    // В данном случае просто заменим ее
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(HistoryModel historyModel);

    // Удалим данные истории
    @Delete
    void deleteHistory(HistoryModel historyModel);

    // Заберем данные по всей истории
    @Query("SELECT * FROM historymodel ORDER BY id DESC")
    List<HistoryModel> getAllHistory();

    //Получить количество записей в таблице
    @Query("SELECT COUNT() FROM historymodel")
    long getCountHistory();
}
