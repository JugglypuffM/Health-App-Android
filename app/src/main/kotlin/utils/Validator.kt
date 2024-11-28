package utils

import arrow.core.raise.result
import domain.Account

class Validator {
    class InvalidNameException(message: String) : Exception(message)
    class InvalidLoginException(message: String) : Exception(message)
    class InvalidPasswordException(message: String) : Exception(message)
    class NotEqualPasswordException(message: String) : Exception(message)

    /**
     * Валидация имени
     * @param name имя пользователя
     */
    private fun validateName(name: String): Result<String> {
        return if (name.isBlank())
            Result.failure(InvalidNameException("User name is empty"))
        else Result.success(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    private fun validateLogin(login: String): Result<String> {
        return if (login.isBlank())
            Result.failure(InvalidLoginException("Login is empty"))
        else Result.success(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    private fun validatePassword(password: String): Result<String> {
        return if (password.length < 6)
            Result.failure(InvalidPasswordException("The password must be at least 6 characters long"))
        else Result.success(password)
    }

    /**
     * Валидация совпадения пароля
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     */
    private fun validateConfirmPassword(password: String, confirmPassword: String): Result<String> {
        return if (password != confirmPassword)
            Result.failure(NotEqualPasswordException("Passwords do not match"))
        else Result.success(password)
    }

    /**
     * Проверка валидности логина и пароля
     * @param rawLogin Логин пользователя
     * @param rawPassword Пароль пользователя
     */
    fun check(rawLogin: String, rawPassword: String): Result<Account> {
        return result {
            val login = validateLogin(rawLogin).bind()
            val password = validatePassword(rawPassword).bind()
            Account(login, password)
        }
    }

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param rawName Имя пользователя
     * @param rawLogin Логин пользователя
     * @param rawPassword Пароль пользователя
     * @param rawConfirmPassword Подтверждение пароля пользователя
     */
    fun check(rawName: String, rawLogin: String, rawPassword: String, rawConfirmPassword: String): Result<Account> {
        return result {
            validateName(rawName).bind()
            val login = validateLogin(rawLogin).bind()
            validateConfirmPassword(rawPassword, rawConfirmPassword).bind()
            val password = validatePassword(rawPassword).bind()
            Account(login, password)
        }
    }
}