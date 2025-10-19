package com.example.myfristapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfristapplication.data.ActionRecord
import com.example.myfristapplication.data.ActionRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(private val actionRecordRepository: ActionRecordRepository) : ViewModel() {
    private val _records = MutableStateFlow<List<ActionRecord>>(emptyList())
    val records: StateFlow<List<ActionRecord>> = _records

    fun requestRecords() {
        viewModelScope.launch {
            _records.value = actionRecordRepository.getAll()
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            actionRecordRepository.deleteAll()
            _records.value = emptyList()
        }
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
}
