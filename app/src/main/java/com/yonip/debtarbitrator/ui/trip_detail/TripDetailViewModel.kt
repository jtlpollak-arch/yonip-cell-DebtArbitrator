package com.yonip.debtarbitrator.ui.trip_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonip.debtarbitrator.dao.ExpenseDao
import com.yonip.debtarbitrator.models.Expense
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class TripDetailViewModel(
    private val expenseDao: ExpenseDao,
    private val destinationId: UUID
) : ViewModel() {

    // שליפת ההוצאות בזמן אמת מה-DB
    val expenses: StateFlow<List<Expense>> = expenseDao.getExpensesByDestination(destinationId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}