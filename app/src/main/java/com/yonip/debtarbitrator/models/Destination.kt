package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(
    tableName = "destinations",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Destination::class,
            parentColumns = ["id"],
            childColumns = ["parentDestinationId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Destination(
    @PrimaryKey val id: UUID,
    val parentDestinationId: UUID?,
    val tripId: UUID,
    val name: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String,
    val arrivalDate:  Long = System.currentTimeMillis(),
    val departureDate : Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)