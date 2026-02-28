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
// ייבוא נכון לפי המבנה בגיט שלך (ui/trip_details)
import com.yonip.debtarbitrator.ui.trip_details.TripDetailScreen
import com.yonip.debtarbitrator.ui.trip_details.TripDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "debt-arbitrator-db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

        val seeder = DatabaseSeeder(db)

        lifecycleScope.launch {
            seeder.seedAll()
            // הזרעת נתוני הרגרסיה כדי שיופיעו הוצאות במסך הפירוט
            seeder.seedRegressionSuite()
        }

        setContent {
            MaterialTheme {
                var currentUser by remember { mutableStateOf<User?>(null) }
                var selectedTripId by remember { mutableStateOf<UUID?>(null) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    val users = withContext(Dispatchers.IO) { db.userDao().getAllUsersOnce() }
                    currentUser = users.firstOrNull()
                    isLoading = false
                }

                if (isLoading) {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                } else if (currentUser == null) {
                    val registrationFactory = ViewModelFactory(db)
                    RegistrationScreen(
                        viewModel = viewModel(factory = registrationFactory),
                        onUserCreated = { currentUser = it }
                    )
                } else {
                    if (selectedTripId == null) {
                        val dashboardFactory = ViewModelFactory(db)
                        DashboardScreen(
                            user = currentUser!!,
                            viewModel = viewModel(factory = dashboardFactory),
                            onTripClick = { tripId -> selectedTripId = tripId }
                        )
                    } else {
                        // ID של היעד (לונדון) כפי שהוגדר ב-seedRegressionSuite
                        val londonDestId = UUID.fromString("22222222-2222-2222-2222-222222222222")

                        // יצירת Factory עם ה-destinationId
                        val detailFactory = ViewModelFactory(db, londonDestId)

                        // קריאה למסך הפירוט עם ה-ViewModel המתאים
                        TripDetailScreen(
                            viewModel = viewModel(factory = detailFactory)
                        )
                    }
                }
            }
        }
    }
}