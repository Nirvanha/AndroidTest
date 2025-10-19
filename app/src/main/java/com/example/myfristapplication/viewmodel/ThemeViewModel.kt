package com.example.myfristapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfristapplication.data.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = ThemePreferences(application)

    // Expose as StateFlow for easy collectAsState in Compose
    val isDarkMode: StateFlow<Boolean> = prefs.isDarkModeFlow.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setDarkMode(dark: Boolean) {
        viewModelScope.launch {
            prefs.setDarkMode(dark)
        }
    }
}

