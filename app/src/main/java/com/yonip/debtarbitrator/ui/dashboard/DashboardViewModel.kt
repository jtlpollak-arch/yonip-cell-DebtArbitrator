package com.yonip.debtarbitrator.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonip.debtarbitrator.dao.TripDao
import com.yonip.debtarbitrator.models.Trip
import com.yonip.debtarbitrator.models.TripParticipant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class DashboardViewModel(private val tripDao: TripDao) : ViewModel() {

    // שליפת כל הטיולים של משתמש ספציפי כ-Flow
    fun getTripsForUser(userId: UUID): Flow<List<Trip>> {
        return tripDao.getTripsForUser(userId)
    }

    // לוגיקה ליצירת טיול חדש (Logic Only)
    fun createTrip(name: String, userId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            val tripId = UUID.randomUUID()

            val newTrip = Trip(id = tripId, name = name, baseCurrency = 1)
            tripDao.insertTrip(newTrip)

            val participant = TripParticipant(
                tripId = tripId,
                userId = userId,
                joinDate = System.currentTimeMillis()
            )
            tripDao.addParticipantToTrip(participant)
        }
    }
}