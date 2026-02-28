package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "expenses",
    foreignKeys = [
        // 1. קישור ליעד (Destination) - במקום לטיול ישירות
        ForeignKey(
            entity = Destination::class,
            parentColumns = ["id"],
            childColumns = ["destinationId"],
            onDelete = ForeignKey.CASCADE
        ),
        // 2. קישור למשתמש (המשלם)
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["participantId"]
        ),
        // 3. קישור לקטגוריה
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ),
        // 4. קישור למטבע
        ForeignKey(
            entity = Currency::class,
            parentColumns = ["id"],
            childColumns = ["currencyId"]
        )
    ]
)
data class Expense(
    @PrimaryKey val id: UUID,
    val destinationId: UUID,    // המפתח הזר ליעד (חייב להתאים ל-childColumns למעלה)
    val participantId: UUID,    // המפתח הזר למשתמש
    val categoryId: Int,        // המפתח הזר לקטגוריה
    val description: String,
    val amount: Double,
    val currencyId: Long,
    val exchangeRate: Double,
    val dateTime: Long,
    val receiptPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)