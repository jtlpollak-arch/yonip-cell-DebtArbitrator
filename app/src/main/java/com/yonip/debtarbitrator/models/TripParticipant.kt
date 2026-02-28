package com.yonip.debtarbitrator.models

import androidx.room.Entity
import java.util.UUID

@Entity(
    tableName = "trip_participants", // וודא שזה כתוב בדיוק ככה, עם s בסוף
    primaryKeys = ["tripId", "userId"],
    // ... foreignKeys ...
)data class TripParticipant(
    val tripId: UUID,
    val userId: UUID,
    val joinDate: Long,
    val leaveDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
