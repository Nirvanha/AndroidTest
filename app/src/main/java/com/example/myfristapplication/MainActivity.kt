package com.example.myfristapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myfristapplication.data.ActionRecord
import com.example.myfristapplication.data.DailyExpense
import com.example.myfristapplication.data.AppDatabase
import com.example.myfristapplication.data.ActionRecordRepository
import com.example.myfristapplication.data.DailyExpenseRepository
import com.example.myfristapplication.ui.theme.MyFristApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var actionRecordRepository: ActionRecordRepository
    private lateinit var dailyExpenseRepository: DailyExpenseRepository
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)
        actionRecordRepository = ActionRecordRepository(database.actionRecordDao())
        dailyExpenseRepository = DailyExpenseRepository(database.dailyExpenseDao())
        viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(actionRecordRepository, dailyExpenseRepository) as T
            }
        }
        val viewModel: MainViewModel by viewModels { viewModelFactory }
        enableEdgeToEdge()
        setContent {
            MyFristApplicationTheme {
                MainApp(viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val message by viewModel.message.collectAsState()
    val records by viewModel.records.collectAsState()
    val expenseRecords by viewModel.expenseRecords.collectAsState()
    val foodDescription by viewModel.foodDescription.collectAsState()
    val dailyExpenseAmountText by viewModel.dailyExpenseAmountText.collectAsState()
    val dailyExpenseCategory by viewModel.dailyExpenseCategory.collectAsState()
    val dailyExpenseOrigin by viewModel.dailyExpenseOrigin.collectAsState()
    val isAmountValid by viewModel.isAmountValid.collectAsState()
    val showExpenseError by viewModel.showExpenseError.collectAsState()

    when (currentScreen) {
        "home" -> HomeScreen(
            onCigaretteClick = {
                viewModel.setMessage("You smoked a cigarette!")
                viewModel.navigateTo("message")
                viewModel.registerAction("cigarette", null)
            },
            onBeerClick = {
                viewModel.setMessage("You drank a beer!")
                viewModel.navigateTo("message")
                viewModel.registerAction("beer", null)
            },
            onFoodClick = {
                viewModel.navigateTo("food")
            },
            onViewRecordsClick = {
                viewModel.requestRecords()
                viewModel.navigateTo("records")
            },
            onDeleteAllClick = {
                viewModel.deleteAllRecords()
            },
            onMoneyClick = {
                viewModel.navigateTo("dailyExpense")
            },
            onViewExpensesClick = {
                viewModel.requestExpenseRecords()
                viewModel.navigateTo("expenseRecords")
            }
        )

        "food" -> FoodScreen(
            description = foodDescription,
            onDescriptionChange = { viewModel.setFoodDescription(it) },
            onRegistrarClick = {
                viewModel.registerAction("food", foodDescription)
                viewModel.setMessage("You register food!")
                viewModel.resetFoodFields()
                viewModel.navigateTo("message")
            },
            onBackClick = {
                viewModel.resetFoodFields()
                viewModel.navigateTo("home")
            }
        )

        "dailyExpense" -> DailyExpenseScreen(
            amountText = dailyExpenseAmountText,
            category = dailyExpenseCategory,
            origin = dailyExpenseOrigin,
            isAmountValid = isAmountValid,
            showExpenseError = showExpenseError,
            onAmountTextChange = { viewModel.setDailyExpenseAmountText(it) },
            onCategoryChange = { viewModel.setDailyExpenseCategory(it) },
            onOriginChange = { viewModel.setDailyExpenseOrigin(it) },
            onRegisterExpenseClick = {
                viewModel.registerExpense()
            },
            onBackClick = {
                viewModel.resetDailyExpenseFields()
                viewModel.navigateTo("home")
            }
        )

        "message" -> MessageScreen(
            message = message,
            onBackClick = { viewModel.navigateTo("home") }
        )

        "records" -> RecordsScreen(
            records = records,
            onBackClick = { viewModel.navigateTo("home") }
        )

        "expenseRecords" -> ExpenseRecordsScreen(
            expenseRecords = expenseRecords,
            onBackClick = { viewModel.navigateTo("home") }
        )
    }
}

@Composable
fun HomeScreen(
    onCigaretteClick: () -> Unit,
    onBeerClick: () -> Unit,
    onFoodClick: () -> Unit,
    onViewRecordsClick: () -> Unit,
    onDeleteAllClick: () -> Unit,
    onMoneyClick: () -> Unit,
    onViewExpensesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onCigaretteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cigarette),
                contentDescription = "Cigarette",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cigarette")
        }

        Button(
            onClick = onBeerClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_beer),
                contentDescription = "Beer",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Beer")
        }

        Button(
            onClick = onFoodClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_food),
                contentDescription = "Beer",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Food")
        }
        Button(
            onClick = onMoneyClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_money),
                contentDescription = "Money",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Money")
        }
        Button(
            onClick = onViewExpensesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Ver gastos")
        }
        Button(
            onClick = onViewRecordsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver registros")
        }

        Button(
            onClick = onDeleteAllClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Borrar registros")
        }
    }
}

@Composable
fun FoodScreen(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onRegistrarClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "¿Qué has comido?", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRegistrarClick, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar comida")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

@Composable
fun MessageScreen(message: String, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}

@Composable
fun RecordsScreen(records: List<ActionRecord>, onBackClick: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Registros", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(records) { record ->
                val formattedDate = dateFormat.format(Date(record.timestamp))
                if (record.type == "comida" && !record.description.isNullOrBlank()) {
                    Text("Comida - $formattedDate - ${record.description}", fontSize = 16.sp)
                } else {
                    Text("${record.type} - $formattedDate", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}

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
