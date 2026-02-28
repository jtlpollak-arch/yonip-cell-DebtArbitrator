package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "trips",
    foreignKeys = [
        ForeignKey(
            entity = Currency::class,
            parentColumns = ["id"],
            childColumns = ["baseCurrency"]
        )
    ]
)
data class Trip(
    @PrimaryKey val id: UUID,
    val name: String,
    val baseCurrency: Long // currencyId
)