package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.UUID


@Entity(
    tableName = "expense_splits",
    primaryKeys = ["expenseId", "userId"],
    foreignKeys = [
        ForeignKey(entity = Expense::class, parentColumns = ["id"], childColumns = ["expenseId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
    ]
)
data class ExpenseSplit(
    val expenseId: UUID,
    val userId: UUID,
    val amount: Double
)