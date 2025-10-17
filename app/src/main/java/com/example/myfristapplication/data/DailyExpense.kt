package com.example.myfristapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_expense")
data class DailyExpense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: Long,
    val note: String? = null,
    val origin: String? = null //TODO: en el futuro poner un enum con tarjetas/cuentas/paypal....
)

