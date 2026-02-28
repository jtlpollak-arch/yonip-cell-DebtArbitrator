package com.yonip.debtarbitrator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yonip.debtarbitrator.db.AppDatabase
import com.yonip.debtarbitrator.ui.dashboard.DashboardViewModel
import com.yonip.debtarbitrator.ui.registration.RegistrationViewModel
import com.yonip.debtarbitrator.ui.trip_detail.TripDetailViewModel

class ViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) ->
                RegistrationViewModel(db.userDao()) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(db.tripDao()) as T
            modelClass.isAssignableFrom(TripDetailViewModel::class.java) ->
                TripDetailViewModel(db.tripDao(), db.expenseDao()) as T // הזרקת ה-DAOs הרלוונטיים
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}