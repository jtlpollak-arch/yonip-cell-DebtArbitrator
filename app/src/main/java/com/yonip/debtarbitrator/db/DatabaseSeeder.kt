package com.yonip.debtarbitrator.db

import com.yonip.debtarbitrator.models.Expense
import com.yonip.debtarbitrator.models.Trip
import com.yonip.debtarbitrator.models.TripParticipant
import com.yonip.debtarbitrator.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class DatabaseSeeder(private val db: AppDatabase) {

    /**
     * פונקציה ראשית להרצת כל נתוני התשתית.
     * ניתן לקרוא לה בכל פעם שהאפליקציה עולה או אחרי רישום משתמש.
     */
    suspend fun seedAll() = withContext(Dispatchers.IO) {
        seedCurrencies()
        seedCategories()
        // כאן תוכל להוסיף בעתיד פונקציות כמו seedCategories() או seedDefaultUsers()
    }


    private fun seedCategories() {
        val categories = listOf(
            1 to "אוכל",
            2 to "תחבורה",
            3 to "לינה",
            4 to "אטרקציות",
            5 to "שונות"
        )

        categories.forEach { (id, name) ->
            db.openHelper.writableDatabase.execSQL(
                "INSERT OR IGNORE INTO categories (id, name) VALUES ($id, '$name')"
            )
        }
    }

    /**
     * הכנסת מטבעות ברירת מחדל.
     * שימוש ב-SQL ישיר כדי לוודא שזה קורה בצורה אטומית ללא צורך ב-DAOs מורכבים.
     */
    private fun seedCurrencies() {
        val currencies = listOf(
            Triple(1, "ILS", "שקל חדש"),
            Triple(2, "USD", "Dollar"),
            Triple(3, "EUR", "Euro")
        )

        currencies.forEach { (id, code, name) ->
            db.openHelper.writableDatabase.execSQL(
                "INSERT OR IGNORE INTO currencies (id, code, name, exchangeRate) VALUES ($id, '$code', '$name', 1.0)"
            )
        }
    }

    suspend fun seedRegressionSuite() = withContext(Dispatchers.IO) {

        val REGRESSION_TRIP_ID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val USER_A_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        val USER_B_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")

        // 1. יצירת משתמשים (אם לא קיימים)
        val userA = User(id = USER_A_ID, name = "ישראל ישראלי", email = "israel@test.com")
        val userB = User(id = USER_B_ID, name = "דנה לוי", email = "dana@test.com")
        db.userDao().insertUser(userA)
        db.userDao().insertUser(userB)

        // 2. יצירת טיול
        val testTrip =
            Trip(id = REGRESSION_TRIP_ID, name = "טיול רגרסיה: לונדון 2026", baseCurrency = 1)
        db.tripDao().insertTrip(testTrip)

        // 3. חיבור המשתמשים לטיול
        db.tripDao().addParticipantToTrip(
            TripParticipant(
                REGRESSION_TRIP_ID,
                USER_A_ID,
                System.currentTimeMillis()
            )
        )
        db.tripDao().addParticipantToTrip(TripParticipant(REGRESSION_TRIP_ID, USER_B_ID, System.currentTimeMillis()))

        // 4. הוספת הוצאה לדוגמה (למשל: ארוחת ערב ב-200 ש"ח ששולמה ע"י ישראל)
        val expenseId = UUID.randomUUID()
        val tripId = REGRESSION_TRIP_ID
        val paidByUserId = USER_A_ID


        val dinnerExpense = Expense(
            id = UUID.randomUUID(),
            destinationId = REGRESSION_TRIP_ID,
            participantId = USER_A_ID, // המשלם
            categoryId = 1, // למשל: מזון
            description = "ארוחת ערב - Regression Test",
            amount = 200.0,
            currencyId = 1L, // שים לב ל-L עבור Long
            exchangeRate = 1.0,
            dateTime = System.currentTimeMillis()
            // שאר השדות (receiptPath, createdAt, updatedAt) יקבלו ערכי ברירת מחדל מהמחלקה
        )
        db.expenseDao().insertExpense(dinnerExpense)

        // כאן בעתיד נוסיף את ה-Splits (איך ה-200 ש"ח מתחלקים)
    }
}