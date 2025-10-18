package com.example.myfristapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfristapplication.data.ActionRecord
import com.example.myfristapplication.data.ActionRecordRepository
import com.example.myfristapplication.data.DailyExpense
import com.example.myfristapplication.data.DailyExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val actionRecordRepository: ActionRecordRepository,
    private val dailyExpenseRepository: DailyExpenseRepository
) : ViewModel() {
    private val _currentScreen = MutableStateFlow("home")
    val currentScreen: StateFlow<String> = _currentScreen

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _records = MutableStateFlow<List<ActionRecord>>(emptyList())
    val records: StateFlow<List<ActionRecord>> = _records

    private val _foodDescription = MutableStateFlow("")
    val foodDescription: StateFlow<String> = _foodDescription

    private val _dailyExpenseAmountText = MutableStateFlow("")
    val dailyExpenseAmountText: StateFlow<String> = _dailyExpenseAmountText

    private val _dailyExpenseCategory = MutableStateFlow("")
    val dailyExpenseCategory: StateFlow<String> = _dailyExpenseCategory

    private val _dailyExpenseOrigin = MutableStateFlow("")
    val dailyExpenseOrigin: StateFlow<String> = _dailyExpenseOrigin

    private val _isAmountValid = MutableStateFlow(true)
    val isAmountValid: StateFlow<Boolean> = _isAmountValid

    private val _showExpenseError = MutableStateFlow(false)
    val showExpenseError: StateFlow<Boolean> = _showExpenseError

    // Añadimos el StateFlow para los registros de gastos diarios
    private val _expenseRecords = MutableStateFlow<List<DailyExpense>>(emptyList())
    val expenseRecords: StateFlow<List<DailyExpense>> = _expenseRecords

    // Función para solicitar los gastos desde el repositorio
    fun requestExpenseRecords() {
        viewModelScope.launch {
            _expenseRecords.value = dailyExpenseRepository.getAll()
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun setMessage(msg: String) {
        _message.value = msg
    }

    fun setFoodDescription(desc: String) {
        _foodDescription.value = desc
    }

    fun setDailyExpenseAmountText(text: String) {
        _dailyExpenseAmountText.value = text
        _isAmountValid.value = text.toDoubleOrNull() != null && text.toDoubleOrNull()!! > 0.0
        _showExpenseError.value = false
    }

    fun setDailyExpenseCategory(cat: String) {
        _dailyExpenseCategory.value = cat
        _showExpenseError.value = false
    }

    fun setDailyExpenseOrigin(origin: String) {
        _dailyExpenseOrigin.value = origin
        _showExpenseError.value = false
    }

    fun registerAction(type: String, description: String?) {
        val record = ActionRecord(
            type = type,
            timestamp = System.currentTimeMillis(),
            description = description
        )
        viewModelScope.launch {
            actionRecordRepository.insert(record)
        }
    }

    fun requestRecords() {
        viewModelScope.launch {
            _records.value = actionRecordRepository.getAll()
        }
    }

    fun deleteAllRecords() {
        viewModelScope.launch {
            actionRecordRepository.deleteAll()
            _records.value = emptyList()
        }
    }

    fun registerExpense() {
        val amount = _dailyExpenseAmountText.value.toDoubleOrNull() ?: 0.0
        if (_isAmountValid.value && _dailyExpenseCategory.value.isNotBlank() && _dailyExpenseOrigin.value.isNotBlank()) {
            val expense = DailyExpense(
                amount = amount,
                category = _dailyExpenseCategory.value,
                date = System.currentTimeMillis(),
                note = null,
                origin = _dailyExpenseOrigin.value
            )
            viewModelScope.launch {
                dailyExpenseRepository.insert(expense)
            }
            _message.value = "Gasto diario registrado!"
            _dailyExpenseAmountText.value = ""
            _dailyExpenseCategory.value = ""
            _dailyExpenseOrigin.value = ""
            _showExpenseError.value = false
            _currentScreen.value = "message"
        } else {
            _showExpenseError.value = true
        }
    }

    fun resetDailyExpenseFields() {
        _dailyExpenseAmountText.value = ""
        _dailyExpenseCategory.value = ""
        _dailyExpenseOrigin.value = ""
        _showExpenseError.value = false
    }

    fun resetFoodFields() {
        _foodDescription.value = ""
    }
}
