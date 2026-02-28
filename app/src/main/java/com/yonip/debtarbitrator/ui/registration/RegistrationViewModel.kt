package com.yonip.debtarbitrator.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonip.debtarbitrator.dao.UserDao
import com.yonip.debtarbitrator.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class RegistrationViewModel(private val userDao: UserDao) : ViewModel() {

    // פונקציה ליצירת משתמש חדש
    fun registerUser(name: String, email: String, onComplete: (User) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUser = User(
                id = UUID.randomUUID(),
                name = name,
                email = email
            )
            userDao.insertUser(newUser)

            // חוזרים ל-Main Thread כדי לעדכן את ה-UI
            launch(Dispatchers.Main) {
                onComplete(newUser)
            }
        }
    }
}