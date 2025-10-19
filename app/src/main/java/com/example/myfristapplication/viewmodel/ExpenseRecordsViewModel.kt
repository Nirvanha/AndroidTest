package com.example.myfristapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfristapplication.data.DailyExpense
import com.example.myfristapplication.data.DailyExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseRecordsViewModel @Inject constructor(private val dailyExpenseRepository: DailyExpenseRepository) : ViewModel() {
    private val _expenseRecords = MutableStateFlow<List<DailyExpense>>(emptyList())
    val expenseRecords: StateFlow<List<DailyExpense>> = _expenseRecords

    fun requestExpenseRecords() {
        viewModelScope.launch {
            _expenseRecords.value = dailyExpenseRepository.getAll()
        }
    }
}
