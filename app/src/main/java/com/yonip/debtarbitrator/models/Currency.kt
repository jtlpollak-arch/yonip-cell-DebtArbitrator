package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey val id: Long,
    val code: String,
    val name: String,
    val exchangeRate: Double
)