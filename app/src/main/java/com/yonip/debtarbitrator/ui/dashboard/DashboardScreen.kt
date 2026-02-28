package com.yonip.debtarbitrator.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yonip.debtarbitrator.models.Trip
import com.yonip.debtarbitrator.models.User
import java.util.UUID

@Composable
fun DashboardScreen(
    user: User,
    viewModel: DashboardViewModel,
    onTripClick: (UUID) -> Unit
) {
    // הרשמה לזרם הטיולים של המשתמש
    val trips by viewModel.getTripsForUser(user.id).collectAsState(initial = emptyList())
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "טיול חדש")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                text = "הטיולים של ${user.name}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (trips.isEmpty()) {
                Text("עדיין אין טיולים רשומים.")
            } else {
                LazyColumn {
                    items(trips) { trip ->
                        TripCard(trip = trip, onClick = { onTripClick(trip.id) })
                    }
                }
            }
        }
    }

    // דיאלוג ליצירת טיול חדש
    if (showCreateDialog) {
        CreateTripDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name ->
                viewModel.createTrip(name, user.id)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun TripCard(trip: Trip, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text = trip.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun CreateTripDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("יצירת טיול חדש") },
        text = {
            Column {
                Text("איך נקרא לטיול?")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("שם הטיול") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name) }
            ) {
                Text("צור")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ביטול")
            }
        }
    )
}