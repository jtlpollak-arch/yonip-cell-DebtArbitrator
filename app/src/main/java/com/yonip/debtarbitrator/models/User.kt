package com.yonip.debtarbitrator.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)] // מבטיח שאי אפשר להכניס אותו אימייל פעמיים
)
data class User(
    @PrimaryKey val id: UUID,
    val name: String,
    val email: String,
    val createdAt: Long = System.currentTimeMillis()
)