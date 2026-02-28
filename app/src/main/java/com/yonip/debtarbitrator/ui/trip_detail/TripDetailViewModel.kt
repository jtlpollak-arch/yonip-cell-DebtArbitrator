package com.yonip.debtarbitrator.ui.trip_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonip.debtarbitrator.dao.ExpenseDao
import com.yonip.debtarbitrator.dao.TripDao
import com.yonip.debtarbitrator.models.Expense
import com.yonip.debtarbitrator.models.Trip
import com.yonip.debtarbitrator.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class TripDetailViewModel(
    private val tripDao: TripDao,
    private val expenseDao: ExpenseDao
) : ViewModel() {

    // שליפת פרטי הטיול
    suspend fun getTrip(tripId: UUID): Trip? {
        return tripDao.getTripById(tripId)
    }

    // זרם המשתתפים בטיול (כדי לדעת בין מי לפצל הוצאות)
    fun getParticipants(tripId: UUID): Flow<List<User>> {
        return tripDao.getParticipantsInTrip(tripId)
    }

    // זרם ההוצאות בטיול (נשלוף כרגע את כל ההוצאות הקשורות לטיול)
    // הערה: נצטרך להוסיף שאילתה מתאימה ב-ExpenseDao בהמשך
    // fun getExpenses(tripId: UUID): Flow<List<Expense>>

    // הוספת הוצאה חדשה
    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.insertExpense(expense)
        }
    }
}