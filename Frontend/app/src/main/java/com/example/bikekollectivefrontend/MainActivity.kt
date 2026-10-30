package com.example.bikekollectivefrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bikekollectivefrontend.ui.theme.BikeKollectiveFrontendTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BikeKollectiveFrontendTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BikeListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// DATA
data class Bike(
    val id: Int,
    val brand: String,
    val model: String,
    val type: String,
    val condition: String,
    val status: String
)

data class NewBike(
    val brand: String,
    val model: String,
    val type: String,
    val condition: String,
    val status: String
)

// FUNCTIONS
@Composable
fun BikeListScreen(modifier: Modifier = Modifier) {

    var bikes by remember {
        mutableStateOf<List<Bike>>(emptyList())
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        try {
            bikes = RetrofitClient.api.getBikes()
            errorMessage = null
        } catch (error: Exception) {
            errorMessage = "Can't connect to database"
            error.printStackTrace()
        }
    }


    val scope = rememberCoroutineScope()

    fun deleteBike(id: Int) {
        scope.launch {
            try {
                RetrofitClient.api.deleteBike(id)
                bikes = bikes.filter { it.id != id }
                errorMessage = null
            } catch (error: Exception) {
                errorMessage = "Failed to delete bike"
                error.printStackTrace()
            }
        }
    }
    LazyColumn(modifier = modifier) {
        item {
            Text("Available Bikes",
            fontWeight = FontWeight.Bold)
        }

        if (errorMessage != null) {
            item {
                Text(errorMessage!!)
            }
        }

        items(bikes, key = { it.id }) { bike ->
            BikeCard(
                bike = bike,
                onDelete = { deleteBike(bike.id) }
            )
        }
        item {
            AddBikeForm()
        }
    }
}

@Composable
fun AddBikeForm() {
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }


    val scope = rememberCoroutineScope()

    Column {
        Text("Add a New Bike",
        fontWeight = FontWeight.Bold)

        TextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand") }
        )

        TextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") }
        )

        TextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("type") }
        )

        TextField(
            value = condition,
            onValueChange = { condition = it },
            label = { Text("condition") }
        )

        TextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("status") }
        )

        Button(onClick = {
            scope.launch {
                try {
                    val newBike = NewBike(
                        brand = brand,
                        model = model,
                        type = type,
                        condition = condition,
                        status = status
                    )

                    RetrofitClient.api.addBike(newBike)

                    println("Bike added successfully!")

                } catch (error: Exception) {
                    error.printStackTrace()
                }
            }
        }) {
            Text("Add Bike")

        }
    }
}

@Composable
fun BikeCard(bike: Bike, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "${bike.brand} ${bike.model}",
                fontWeight = FontWeight.Bold
            )

            Text("Bike ID: ${bike.id}")
            Text("Type: ${bike.type}")
            Text("Condition: ${bike.condition}")
            Text("Status: ${bike.status}")

            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BikeKollectiveFrontendTheme {
        BikeListScreen()
    }
}