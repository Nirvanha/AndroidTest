package com.example.myfristapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "action_record")
data class ActionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val timestamp: Long,
    val description: String? = null // Nuevo campo para comida
)
