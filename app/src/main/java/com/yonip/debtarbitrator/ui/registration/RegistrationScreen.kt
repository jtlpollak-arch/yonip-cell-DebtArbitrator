package com.yonip.debtarbitrator.ui.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yonip.debtarbitrator.models.User

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    onUserCreated: (User) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ברוכים הבאים", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("שם מלא") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("אימייל") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank()) {
                    viewModel.registerUser(name, email, onUserCreated)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("התחל לעבוד")
        }
    }
}