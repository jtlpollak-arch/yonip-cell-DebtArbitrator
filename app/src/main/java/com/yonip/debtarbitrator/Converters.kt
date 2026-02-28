package com.yonip.debtarbitrator

import androidx.room.TypeConverter
import java.util.UUID

class Converters {
    // הופך UUID למחרוזת כדי שיישמר ב-DB
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    // הופך מחרוזת חזרה ל-UUID כששולפים מה-DB
    @TypeConverter
    fun toUUID(uuidString: String?): UUID? {
        return uuidString?.let { UUID.fromString(it) }
    }
}