package com.example.myfristapplication.ui.records

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfristapplication.data.DailyExpense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseRecordsScreen(expenseRecords: List<DailyExpense>, onBackClick: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Gastos", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(expenseRecords) { expense ->
                val formattedDate = dateFormat.format(Date(expense.date))
                val noteText = expense.note?.let { " - $it" } ?: ""
                Text("${expense.amount} € - ${expense.category} - $formattedDate - ${expense.origin}$noteText", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Button(onClick = onBackClick) {
            Text("Volver")
        }
    }
}

