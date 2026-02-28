package com.yonip.debtarbitrator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yonip.debtarbitrator.models.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {

    // --- יצירה ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertUsers(users: List<User>)

    // --- עדכון ---
    @Update
    suspend fun updateUser(user: User)

    // --- מחיקה ---
    @Delete
    suspend fun deleteUser(user: User)

    // --- שליפה (Queries) ---
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: UUID): User?

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()


    @Query("SELECT * FROM users")
    suspend fun getAllUsersOnce(): List<User>
}