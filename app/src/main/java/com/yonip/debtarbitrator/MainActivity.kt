package com.yonip.debtarbitrator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.yonip.debtarbitrator.db.AppDatabase
import com.yonip.debtarbitrator.db.DatabaseSeeder
import com.yonip.debtarbitrator.models.User
import com.yonip.debtarbitrator.ui.ViewModelFactory
import com.yonip.debtarbitrator.ui.dashboard.DashboardScreen
import com.yonip.debtarbitrator.ui.registration.RegistrationScreen
import com.yonip.debtarbitrator.ui.trip_detail.TripDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            applicationContext, // וודא שזה הפרמטר הראשון
            AppDatabase::class.java,
            "debt-arbitrator-db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

        val seeder = DatabaseSeeder(db)

        // הרצה ב-Coroutine
        lifecycleScope.launch {
            seeder.seedAll()
            // אם אתה במצב פיתוח ורוצה נתוני רגרסיה:
            // seeder.seedRegressionData()
        }

        setContent {
            MaterialTheme {
                var currentUser by remember { mutableStateOf<User?>(null) }
                var selectedTripId by remember { mutableStateOf<UUID?>(null) } // מצב הניווט
                var isLoading by remember { mutableStateOf(true) }
                val factory = ViewModelFactory(db)

                LaunchedEffect(Unit) {
                    val users = withContext(Dispatchers.IO) { db.userDao().getAllUsersOnce() }
                    currentUser = users.firstOrNull()
                    isLoading = false
                }

                if (isLoading) {
                    Box(Modifier.fillMaxSize()) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
                } else if (currentUser == null) {
                    RegistrationScreen(
                        viewModel = viewModel(factory = factory),
                        onUserCreated = { currentUser = it }
                    )
                } else {
                    // לוגיקת הניווט:
                    if (selectedTripId == null) {
                        // הצגת ה-Dashboard
                        DashboardScreen(
                            user = currentUser!!,
                            viewModel = viewModel(factory = factory),
                            onTripClick = { tripId -> selectedTripId = tripId } // מעבר למסך פירוט
                        )
                    } else {
                        // הצגת מסך פירוט הטיול
                        TripDetailScreen(
                            tripId = selectedTripId!!,
                            viewModel = viewModel(factory = factory),
                            onBack = { selectedTripId = null } // חזרה ל-Dashboard
                        )
                    }
                }
            }
        }
    }
}