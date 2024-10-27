package utils

import android.util.Log
import domain.User

//TODO реализовать базу данных для хранения и извлечения пользователей
class LocalStorage {
    fun getUser(): User {
        return User("John Doe", "john_doe", "password123")
    }

    fun saveUser(user: User) {
        Log.d("DB", "User saved: $user")
    }

    fun dropUser() {
        Log.d("DB", "user dropped")
    }
}