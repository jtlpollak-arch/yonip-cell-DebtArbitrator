package com.yonip.debtarbitrator.db

import com.yonip.debtarbitrator.models.Destination
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
        seedRegressionSuite()
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

        // 1. הגדרת מזהים קבועים (Static UUIDs)
        val REGRESSION_TRIP_ID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val MAIN_DEST_ID = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val USER_A_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        val USER_B_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")

        // --- שלב א': ישויות עצמאיות (Users) ---
        val userA = User(id = USER_A_ID, name = "ישראל ישראלי", email = "israel@test.com")
        val userB = User(id = USER_B_ID, name = "דנה לוי", email = "dana@test.com")
        db.userDao().insertUser(userA)
        db.userDao().insertUser(userB)

        // --- שלב ב': הטיול (Trip) ---
        // שים לב: המטבע (baseCurrency = 1) חייב להיות מוזרע כבר ב-seedCurrencies
        val testTrip = Trip(
            id = REGRESSION_TRIP_ID,
            name = "טיול רגרסיה: לונדון 2026",
            baseCurrency = 1
        )
        db.tripDao().insertTrip(testTrip)

        // חיבור המשתמשים לטיול (משתתפים)
        db.tripDao().addParticipantToTrip(TripParticipant(REGRESSION_TRIP_ID, USER_A_ID, System.currentTimeMillis()))
        db.tripDao().addParticipantToTrip(TripParticipant(REGRESSION_TRIP_ID, USER_B_ID, System.currentTimeMillis()))

        // --- שלב ג': היעד (Destination) ---
        // זה האבא הישיר של ההוצאה. חייב להיווצר לפני ה-Expense.
        val londonDest = Destination(
            id = MAIN_DEST_ID,
            parentDestinationId = null, // יעד ראשי
            tripId = REGRESSION_TRIP_ID,
            name = "מרכז לונדון (Piccadilly)",
            countryCode = "GB",
            latitude = 51.509865,
            longitude = -0.134181,
            timezoneId = "Europe/London",
            arrivalDate = System.currentTimeMillis(),
            departureDate = System.currentTimeMillis() + (86400000 * 4) // טיול של 4 ימים
        )
        db.destinationDao().insertDestination(londonDest)

        // --- שלב ד': ההוצאה (Expense) ---
        // עכשיו כשכל המפתחות הזרים (DestinationId, ParticipantId, CategoryId) קיימים - זה יעבוד!
        val dinnerExpense = Expense(
            id = UUID.randomUUID(),
            destinationId = MAIN_DEST_ID,
            participantId = USER_A_ID, // שולם ע"י ישראל
            categoryId = 1,            // "אוכל" (מוזרע ב-seedCategories)
            description = "ארוחת ערב חגיגית - Regression Test",
            amount = 150.0,
            currencyId = 1L,           // שקלים (ILS)
            exchangeRate = 1.0,
            dateTime = System.currentTimeMillis()
        )
        db.expenseDao().insertExpense(dinnerExpense)

        // כאן בעתיד נוסיף ExpenseSplit כדי לדעת שדנה (User B) חייבת חצי לישראל
    }
}