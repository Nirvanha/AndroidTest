package com.example.myfristapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface   ActionRecordDao {
    @Insert
    suspend fun insert(record: ActionRecord)

    @Query("SELECT * FROM action_record ORDER BY timestamp DESC")
    suspend fun getAll(): List<ActionRecord>

    @Query("DELETE FROM action_record")
    suspend fun deleteAll()
}
