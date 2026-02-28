package com.yonip.debtarbitrator.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class formatters {
    fun formatTimestampToDay(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()) // למשל: "יום ראשון, 1 במרץ"
        return formatter.format(date)
    }
}