package com.example.myfristapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface DailyExpenseDao {
    @Insert
    suspend fun insert(dailyExpense: DailyExpense): Long

    @Update
    suspend fun update(dailyExpense: DailyExpense)

    @Delete
    suspend fun delete(dailyExpense: DailyExpense)

    @Query("SELECT * FROM daily_expense ORDER BY date DESC")
    suspend fun getAll(): List<DailyExpense>

    @Query("SELECT * FROM daily_expense WHERE id = :id")
    suspend fun getById(id: Int): DailyExpense?
}

