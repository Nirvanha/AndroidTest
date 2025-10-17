package com.example.myfristapplication.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DailyExpenseRepository(private val dao: DailyExpenseDao) {
    suspend fun insert(expense: DailyExpense): Long = withContext(Dispatchers.IO) {
        dao.insert(expense)
    }

    suspend fun update(expense: DailyExpense) = withContext(Dispatchers.IO) {
        dao.update(expense)
    }

    suspend fun delete(expense: DailyExpense) = withContext(Dispatchers.IO) {
        dao.delete(expense)
    }

    suspend fun getAll(): List<DailyExpense> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun getById(id: Int): DailyExpense? = withContext(Dispatchers.IO) {
        dao.getById(id)
    }
}

