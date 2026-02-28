package com.yonip.debtarbitrator.dao

import androidx.room.*
import com.yonip.debtarbitrator.models.Destination
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface DestinationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDestination(destination: Destination)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDestinations(destinations: List<Destination>)

    // שליפת כל היעדים של טיול מסוים, ממוינים לפי תאריך הגעה
    @Query("SELECT * FROM destinations WHERE tripId = :tripId ORDER BY arrivalDate ASC")
    fun getDestinationsByTrip(tripId: UUID): Flow<List<Destination>>

    // שליפת יעד ספציפי (שימושי למסך פרטי יעד)
    @Query("SELECT * FROM destinations WHERE id = :destinationId")
    suspend fun getDestinationById(destinationId: UUID): Destination?

    @Delete
    suspend fun deleteDestination(destination: Destination)

    // מחיקת כל היעדים של טיול (אם כי ה-CASCADE בדרך כלל מטפל בזה)
    @Query("DELETE FROM destinations WHERE tripId = :tripId")
    suspend fun deleteDestinationsByTrip(tripId: UUID)
}