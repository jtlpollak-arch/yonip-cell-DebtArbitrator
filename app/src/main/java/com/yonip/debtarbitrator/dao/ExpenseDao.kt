package com.yonip.debtarbitrator.dao

import androidx.room.*
import com.yonip.debtarbitrator.models.Expense
import com.yonip.debtarbitrator.models.ExpenseSplit
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ExpenseDao {

    // --- יצירת הוצאה ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    // --- הוספת פיצול (מי חייב כמה) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplits(splits: List<ExpenseSplit>)

    // --- שליפה ברזולוציה יומית ---
    // השאילתה הזו שולפת את כל ההוצאות של תחנה מסוימת וממיינת לפי זמן
    @Query("SELECT * FROM expenses WHERE destinationId = :destinationId ORDER BY dateTime ASC")
    fun getExpensesByDestination(destinationId: UUID): Flow<List<Expense>>

    // --- חישובים פיננסיים (Aggregation) ---

    // סיכום הוצאות כולל לטיול (במטבע הבסיס - דורש הכפלה ב-exchangeRate)
    @Query("""
        SELECT SUM(amount * exchangeRate) 
        FROM expenses 
        INNER JOIN destinations ON expenses.destinationId = destinations.id 
        WHERE destinations.tripId = :tripId
    """)
    fun getTotalTripAmount(tripId: UUID): Flow<Double?>

    // סיכום הוצאות יומי לתחנה מסוימת
    // כאן אנחנו משתמשים בפונקציות תאריך של SQLite (במילישניות)
    @Query("""
        SELECT SUM(amount) 
        FROM expenses 
        WHERE destinationId = :destinationId 
        AND dateTime >= :startOfDay AND dateTime <= :endOfDay
    """)
    suspend fun getDailySum(destinationId: UUID, startOfDay: Long, endOfDay: Long): Double?

    // --- מחיקת הוצאה (תמחק אוטומטית גם את הפיצולים בגלל ה-CASCADE) ---
    @Delete
    suspend fun deleteExpense(expense: Expense)
}