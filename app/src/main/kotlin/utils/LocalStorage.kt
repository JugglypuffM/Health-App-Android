package utils

import android.util.Log
import domain.Either
import domain.User

/**
 * Локальное хранилище
 */
//TODO реализовать базу данных для хранения и извлечения пользователей
class LocalStorage {
    class UserNotFoundException(message: String) : Exception(message)

    fun loadUser(): Either<Throwable, User> {
        return Either.Right(User("John Doe", "john_doe", "password123"))
    }

    fun saveUser(user: User) {
        Log.d("DB", "User saved: $user")
    }

    fun dropUser() {
        Log.d("DB", "user dropped")
    }
}