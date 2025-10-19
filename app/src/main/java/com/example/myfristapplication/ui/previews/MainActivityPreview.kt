package com.example.myfristapplication.ui.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfristapplication.data.ActionRecord
import com.example.myfristapplication.data.DailyExpense
import com.example.myfristapplication.ui.expense.DailyExpenseScreen
import com.example.myfristapplication.ui.food.FoodScreen
import com.example.myfristapplication.ui.home.HomeScreen
import com.example.myfristapplication.ui.records.ExpenseRecordsScreen
import com.example.myfristapplication.ui.records.RecordsScreen
import com.example.myfristapplication.ui.shared.MessageScreen

// Preview that reproduces MainApp navigation using simple local state and sample data.
@Composable
fun PreviewMainApp() {
    _root_ide_package_.com.example.myfristapplication.ui.theme.MyFristApplicationTheme {
        Surface(modifier = Modifier.padding(0.dp), color = MaterialTheme.colorScheme.background) {
            // simple local state to emulate MainViewModel + other viewmodels
            var currentScreen by remember { mutableStateOf("home") }
            var message by remember { mutableStateOf("") }

            var foodDescription by remember { mutableStateOf("") }

            var amountText by remember { mutableStateOf("") }
            var category by remember { mutableStateOf("") }
            var origin by remember { mutableStateOf("") }
            var isAmountValid by remember { mutableStateOf(true) }
            var showExpenseError by remember { mutableStateOf(false) }

            // sample records
            val sampleRecords = listOf(
                ActionRecord(
                    type = "cigarette",
                    timestamp = System.currentTimeMillis() - 1000L * 60 * 60,
                    description = null
                ),
                ActionRecord(
                    type = "comida",
                    timestamp = System.currentTimeMillis() - 1000L * 60 * 30,
                    description = "Ensalada"
                )
            )

            val sampleExpenses = listOf(
                DailyExpense(
                    amount = 5.5,
                    category = "Comida",
                    date = System.currentTimeMillis() - 1000L * 60 * 60 * 24,
                    note = "Bocadillo",
                    origin = "Máquina"
                ),
                DailyExpense(
                    amount = 2.0,
                    category = "Transporte",
                    date = System.currentTimeMillis() - 1000L * 60 * 60 * 2,
                    note = null,
                    origin = "Bus"
                )
            )

            when (currentScreen) {
                "home" -> HomeScreen(
                    onCigaretteClick = {
                        message = "You smoked a cigarette!"
                        currentScreen = "message"
                    },
                    onBeerClick = {
                        message = "You drank a beer!"
                        currentScreen = "message"
                    },
                    onFoodClick = { currentScreen = "food" },
                    onViewRecordsClick = { currentScreen = "records" },
                    onDeleteAllClick = { /* noop for preview */ },
                    onMoneyClick = { currentScreen = "dailyExpense" },
                    onViewExpensesClick = { currentScreen = "expenseRecords" }
                )

                "food" -> FoodScreen(
                    description = foodDescription,
                    onDescriptionChange = { foodDescription = it },
                    onRegistrarClick = {
                        message = "You register food!"
                        foodDescription = ""
                        currentScreen = "message"
                    },
                    onBackClick = {
                        foodDescription = ""
                        currentScreen = "home"
                    }
                )

                "dailyExpense" -> DailyExpenseScreen(
                    amountText = amountText,
                    category = category,
                    origin = origin,
                    isAmountValid = isAmountValid,
                    showExpenseError = showExpenseError,
                    onAmountTextChange = {
                        amountText = it
                        val parsed = it.toDoubleOrNull()
                        isAmountValid = parsed != null && parsed > 0.0
                        showExpenseError = false
                    },
                    onCategoryChange = { category = it; showExpenseError = false },
                    onOriginChange = { origin = it; showExpenseError = false },
                    onRegisterExpenseClick = {
                        val ok = isAmountValid && category.isNotBlank() && origin.isNotBlank()
                        if (ok) {
                            message = "Gasto diario registrado!"
                            amountText = ""
                            category = ""
                            origin = ""
                            showExpenseError = false
                            currentScreen = "message"
                        } else {
                            showExpenseError = true
                        }
                    },
                    onBackClick = {
                        amountText = ""
                        category = ""
                        origin = ""
                        showExpenseError = false
                        currentScreen = "home"
                    }
                )

                "message" -> MessageScreen(
                    message = message,
                    onBackClick = { currentScreen = "home" }
                )

                "records" -> RecordsScreen(
                    records = sampleRecords,
                    onBackClick = { currentScreen = "home" }
                )

                "expenseRecords" -> ExpenseRecordsScreen(
                    expenseRecords = sampleExpenses,
                    onBackClick = { currentScreen = "home" }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(name = "Main Preview - Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Main Preview - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(device = Devices.PIXEL_7A, showBackground = true)
@Composable
fun PreviewMainApp_Defaults() {
    PreviewMainApp()
}

