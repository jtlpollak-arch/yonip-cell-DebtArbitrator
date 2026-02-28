package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val iconName: String? = null // שם האייקון לשימוש ב-UI בעתיד
)