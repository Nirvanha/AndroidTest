package com.example.myfristapplication.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DailyExpenseScreen(
    amountText: String,
    category: String,
    origin: String,
    isAmountValid: Boolean,
    showExpenseError: Boolean,
    onAmountTextChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onOriginChange: (String) -> Unit,
    onRegisterExpenseClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Registrar gasto diario", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = amountText,
            onValueChange = onAmountTextChange,
            label = { Text("Cantidad") },
            isError = !isAmountValid,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (!isAmountValid) {
            Text(
                text = "Introduce una cantidad válida mayor que 0",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = category,
            onValueChange = onCategoryChange,
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = origin,
            onValueChange = onOriginChange,
            label = { Text("Origen") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRegisterExpenseClick,
            enabled = isAmountValid && category.isNotBlank() && origin.isNotBlank(),
            modifier = Modifier.fillMaxWidth()) {
            Text("Registrar gasto")
        }
        if (showExpenseError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por favor, completa todos los campos.",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

