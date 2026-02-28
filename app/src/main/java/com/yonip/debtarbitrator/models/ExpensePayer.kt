package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.UUID


@Entity(
    tableName = "expense_payers", // שיניתי את השם לדיוק (אלו ששילמו בפועל)
    primaryKeys = ["expenseId", "userId"],
    foreignKeys = [
        ForeignKey(entity = Expense::class, parentColumns = ["id"], childColumns = ["expenseId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
    ]
)
data class ExpensePayer(
    val expenseId: UUID,
    val userId: UUID,
    val amount: Double
)