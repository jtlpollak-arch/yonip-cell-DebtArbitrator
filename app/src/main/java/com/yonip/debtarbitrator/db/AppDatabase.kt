package com.yonip.debtarbitrator.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yonip.debtarbitrator.Converters
import com.yonip.debtarbitrator.dao.DestinationDao
import com.yonip.debtarbitrator.dao.ExpenseDao
import com.yonip.debtarbitrator.dao.TripDao
import com.yonip.debtarbitrator.dao.UserDao
import com.yonip.debtarbitrator.models.Category
import com.yonip.debtarbitrator.models.Currency
import com.yonip.debtarbitrator.models.Destination
import com.yonip.debtarbitrator.models.Expense
import com.yonip.debtarbitrator.models.ExpensePayer
import com.yonip.debtarbitrator.models.ExpenseSplit
import com.yonip.debtarbitrator.models.Trip
import com.yonip.debtarbitrator.models.TripParticipant
import com.yonip.debtarbitrator.models.User

@Database(
    entities = [
        User::class,
        Currency::class,
        Trip::class,
        TripParticipant::class,
        Destination::class,
        Expense::class,
        ExpenseSplit::class,
        ExpensePayer::class,
        Category::class
    ],
    version = 8, // בכל שינוי Schema בעתיד נעלה את המספר
    exportSchema = false
)

@TypeConverters(Converters::class) // כאן הוא אמור לזהות את הקלאס מהקובץ השני
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun destinationDao(): DestinationDao
}