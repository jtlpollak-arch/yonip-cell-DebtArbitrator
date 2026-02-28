package com.yonip.debtarbitrator.dao

import androidx.room.*
import com.yonip.debtarbitrator.models.Trip
import com.yonip.debtarbitrator.models.TripParticipant
import com.yonip.debtarbitrator.models.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TripDao {

    // --- פעולות בסיסיות על הטיול ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip)

    @Update
    suspend fun updateTrip(trip: Trip)

    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: UUID): Trip?

    // --- ניהול משתתפים (טבלת הקשר) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addParticipantToTrip(participant: TripParticipant)

    @Delete
    suspend fun removeParticipantFromTrip(participant: TripParticipant)

    /**
     * שאילתת JOIN בסיסית:
     * שולפת את כל המשתמשים (Users) שרשומים לטיול ספציפי דרך טבלת trip_participants
     */
    @Query("""
        SELECT users.* FROM users 
        INNER JOIN trip_participants ON users.id = trip_participants.userId 
        WHERE trip_participants.tripId = :tripId
    """)
    fun getParticipantsInTrip(tripId: UUID): Flow<List<User>>

    /**
     * שליפת כל הטיולים שמשתמש ספציפי רשום אליהם
     */
    @Query("""
        SELECT trips.* FROM trips 
        INNER JOIN trip_participants ON trips.id = trip_participants.tripId 
        WHERE trip_participants.userId = :userId
    """)
    fun getTripsForUser(userId: UUID): Flow<List<Trip>>
}