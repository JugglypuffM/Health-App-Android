package utils

import android.content.Context
import android.content.SharedPreferences
import arrow.core.raise.result
import domain.Account

/**
 * Локальное хранилище для пользователя
 */
class UserSerializer(private val applicationContext: Context) {
    class UserNotFoundException(message: String) : Exception(message)

    val TABLE_NAME = "User"
    val sharedPreferences = applicationContext.getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)

    private val LOGIN = "login"
    private val PASSWORD = "password"

    private fun getValue(key: String): Result<String> {
        val value =  sharedPreferences.getString(key, null)
        return if (value == null)
            Result.failure(UserNotFoundException("User has incorrect key: ${key}"))
        else
            Result.success(value)
    }

    /**
     * Загрузить пользователя
     */
    fun loadAccount(): Result<Account> {
        return result {
            val login = getValue(LOGIN).bind()
            val password = getValue(PASSWORD).bind()
            Account(login, password)
        }
    }

    /**
     * Сохранить пользователя
     */
    fun saveAccount(user: Account): Result<String>{
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
    fun dropAccount(): Result<String>{
        val editor = sharedPreferences.edit()

        editor.apply{
            editor.clear()
            apply()
        }

        return Result.success("User dropped")
    }
}