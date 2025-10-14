package com.example.myfristapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.myfristapplication.data.ActionRecord
import com.example.myfristapplication.data.AppDatabase
import kotlinx.coroutines.launch
import com.example.myfristapplication.ui.theme.MyFristApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)
        enableEdgeToEdge()
        setContent {
            MyFristApplicationTheme {
                MainApp(
                    onRegisterAction = { actionType ->
                        val record = ActionRecord(type = actionType, timestamp = System.currentTimeMillis())
                        lifecycleScope.launch {
                            database.actionRecordDao().insert(record)
                        }
                    },
                    onRequestRecords = {
                        database.actionRecordDao().getAll()
                    },
                    onDeleteAllRecords = {
                        database.actionRecordDao().deleteAll()
                    }
                )
            }
        }
    }
}

@Composable
fun MainApp(
    onRegisterAction: (String) -> Unit,
    onRequestRecords: suspend () -> List<ActionRecord>,
    onDeleteAllRecords: suspend () -> Unit
) {
    var currentScreen by remember { mutableStateOf("home") }
    var message by remember { mutableStateOf("") }
    var records by remember { mutableStateOf<List<ActionRecord>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    when (currentScreen) {
        "home" -> HomeScreen(
            onCigaretteClick = {
                message = "You smoked a cigarette!"
                currentScreen = "message"
                onRegisterAction("cigarette")
            },
            onBeerClick = {
                message = "You drank a beer!"
                currentScreen = "message"
                onRegisterAction("beer")
            },
            onViewRecordsClick = {
                coroutineScope.launch {
                    records = onRequestRecords()
                    currentScreen = "records"
                }
            },
            onDeleteAllClick = {
                coroutineScope.launch {
                    onDeleteAllRecords()
                    records = emptyList()
                }
            }
        )
        "message" -> MessageScreen(
            message = message,
            onBackClick = { currentScreen = "home" }
        )
        "records" -> RecordsScreen(
            records = records,
            onBackClick = { currentScreen = "home" }
        )
    }
}

@Composable
fun HomeScreen(
    onCigaretteClick: () -> Unit,
    onBeerClick: () -> Unit,
    onViewRecordsClick: () -> Unit,
    onDeleteAllClick: () -> Unit
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
                painter = painterResource(id = R.drawable.ic_cigarro),
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
                painter = painterResource(id = R.drawable.ic_cerveza),
                contentDescription = "Beer",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Beer")
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
                Text("${record.type} - $formattedDate", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}
