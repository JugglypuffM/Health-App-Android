package utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import domain.Either
import domain.User

/**
 * Локальное хранилище
 */
class LocalStorage {
    class UserNotFoundException(message: String) : Exception(message)

    private lateinit var sharedPreferences: SharedPreferences

    private val LOGIN = "login"
    private val PASSWORD = "password"
    private val TABLE_NAME = "password"


    private fun fromNullable(value: String?): Either<Throwable, String>{
        return Either.fromNullable(value, UserNotFoundException("User has incorrectly saved"))
    }

    private fun get(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    /**
     * Загрузить пользователя
     */
    suspend fun loadUser(): Either<Throwable, User> {
        Log.d("MYDB", "Loading user...")
        return fromNullable(get(LOGIN)).flatMap { login ->
            fromNullable(get(PASSWORD)).map { password ->
                User(null, login, password)
            }
        }
    }

    /**
     * Сохранить пользователя
     */
    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()

        editor.putString("LOGIN", user.login)
        editor.putString("PASSWORD", user.password)

        editor.apply()
        Log.d("MYDB", "User saved: $user")
    }

    /**
     * Удалить пользователя
     */
    fun dropUser() {
        val editor = sharedPreferences.edit()
        editor.clear() // Удаляет все данные
        editor.apply() // Применяет изменения
        Log.d("MYDB", "user dropped")
    }

    /**
     * Установить контекст
     */
    fun setContext(context: Context) {
        sharedPreferences = context.getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)
    }
}