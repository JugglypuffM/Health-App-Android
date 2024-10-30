package utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import domain.User
import domain.flatMap

/**
 * Локальное хранилище
 */
class LocalStorage {
    class UserNotFoundException(message: String) : Exception(message)

    private lateinit var sharedPreferences: SharedPreferences

    private val LOGIN = "login"
    private val PASSWORD = "password"
    private val TABLE_NAME = "password"

    private fun getValue(key: String): Result<String> {
        val value =  sharedPreferences.getString(key, null)
        return if (value == null) Result.failure(UserNotFoundException("User has incorrect key: ${key}")) else Result.success(value)
    }

    /**
     * Загрузить пользователя
     */
    suspend fun loadUser(): Result<User> {
        Log.d("MYDB", "Loading user...")
        return getValue(LOGIN).flatMap { login ->
            getValue(PASSWORD).map { password ->
                User(null, login, password)
            }
        }
    }

    /**
     * Сохранить пользователя
     */
    fun saveUser(user: User): Result<String>{
        val editor = sharedPreferences.edit()

        editor.apply {
            editor.putString(LOGIN, user.login)
            editor.putString(PASSWORD, user.password)
            apply()
        }

        return Result.success("User saved")
    }

    /**
     * Удалить пользователя
     */
    fun dropUser(): Result<String>{
        val editor = sharedPreferences.edit()

        editor.apply{
            editor.clear()
            apply()
        }

        return Result.success("User dropped")
    }

    /**
     * Установить контекст
     */
    fun setContext(context: Context) {
        sharedPreferences = context.getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)
    }
}