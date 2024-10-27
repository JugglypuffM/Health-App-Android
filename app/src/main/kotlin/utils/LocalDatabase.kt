package utils

import android.util.Log
import domain.User

//TODO реализовать базу данных для хранения и извлечения пользователей
class LocalDatabase {
    fun getUser() = User("John Doe", "john_doe", "password123")
    fun saveUser(user: User) = Log.d("DB", "User saved: $user")
}