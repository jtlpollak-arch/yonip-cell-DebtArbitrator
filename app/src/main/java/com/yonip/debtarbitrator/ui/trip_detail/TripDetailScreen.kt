package com.yonip.debtarbitrator.ui.trip_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yonip.debtarbitrator.models.Trip
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    tripId: UUID,
    viewModel: TripDetailViewModel,
    onBack: () -> Unit
) {
    var trip by remember { mutableStateOf<Trip?>(null) }

    // טעינת פרטי הטיול מה-DB
    LaunchedEffect(tripId) {
        trip = viewModel.getTrip(tripId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trip?.name ?: "טוען...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "חזרה")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("כאן יופיע פירוט ההוצאות עבור טיול זה", style = MaterialTheme.typography.bodyLarge)

            // בשלב הבא נוסיף כאן רשימת הוצאות וכפתור הוספה
        }
    }
}