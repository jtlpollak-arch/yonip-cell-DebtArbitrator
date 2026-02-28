package com.yonip.debtarbitrator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yonip.debtarbitrator.db.AppDatabase
import com.yonip.debtarbitrator.ui.dashboard.DashboardViewModel
import com.yonip.debtarbitrator.ui.registration.RegistrationViewModel
import com.yonip.debtarbitrator.ui.trip_details.TripDetailViewModel
import java.util.UUID

class ViewModelFactory(
    private val db: AppDatabase,
    private val destinationId: UUID? = null // פרמטר אופציונלי למסך הפירוט
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) -> {
                RegistrationViewModel(db.userDao()) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(db.tripDao()) as T
            }
            modelClass.isAssignableFrom(TripDetailViewModel::class.java) -> {
                // העברה נכונה של ה-DAO וה-ID (חייב להתאים ל-Constructor של ה-ViewModel)
                TripDetailViewModel(db.expenseDao(), destinationId!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}