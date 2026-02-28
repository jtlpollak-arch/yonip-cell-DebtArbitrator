package com.yonip.debtarbitrator.ui.trip_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yonip.debtarbitrator.models.Expense

@Composable
fun TripDetailScreen(viewModel: TripDetailViewModel) {
    val expenses by viewModel.expenses.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: פתיחת מסך הוספה */ }) {
                Icon(Icons.Default.Add, contentDescription = "הוסף הוצאה")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(
                text = "פירוט הוצאות ליעד",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )

            if (expenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("עדיין אין הוצאות. נסה להריץ את ה-Seeder!")
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(expenses) { expense ->
                        ExpenseItem(expense)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = expense.description, fontWeight = FontWeight.SemiBold)
                Text(text = "קטגוריה: ${expense.categoryId}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "${expense.amount} ${if (expense.currencyId == 1L) "ILS" else "GBP"}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}