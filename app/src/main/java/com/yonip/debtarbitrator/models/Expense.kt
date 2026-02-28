package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Destination::class,
            parentColumns = ["id"],
            childColumns = ["destinationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["participantId"] // שיניתי ל-participantId שיתאים לשדה למטה
        ),
        ForeignKey(
            entity = Currency::class,
            parentColumns = ["id"],
            childColumns = ["currencyId"]
        ),
        ForeignKey(
            entity = Category::class, // המפתח הזר החדש לקטגוריה
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )
    ]
)
data class Expense(
    @PrimaryKey val id: UUID,
    val destinationId: UUID,
    val participantId: UUID, // המשלם
    val categoryId: Int,     // המפתח הזר לקטגוריה
    val description: String,
    val amount: Double,
    val currencyId: Long,
    val exchangeRate: Double,
    val dateTime: Long,
    val receiptPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)