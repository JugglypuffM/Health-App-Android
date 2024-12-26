package utils

import arrow.core.raise.result
import domain.Account

class Validator {
    class InvalidNameException(message: String) : Exception(message)
    class InvalidLoginException(message: String) : Exception(message)
    class InvalidPasswordException(message: String) : Exception(message)
    class NotEqualPasswordException(message: String) : Exception(message)
    class InvalidAgeException(message: String) : Exception(message)
    class InvalidWeightException(message: String) : Exception(message)

    /**
     * Валидация имени
     * @param name имя пользователя
     */
    fun validateName(name: String): Result<String> {
        return if (name.isBlank())
            Result.failure(InvalidNameException("User name is empty"))
        else Result.success(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    fun validateLogin(login: String): Result<String> {
        return if (login.isBlank())
            Result.failure(InvalidLoginException("Login is empty"))
        else Result.success(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    fun validatePassword(password: String): Result<String> {
        return if (password.length < 6)
            Result.failure(InvalidPasswordException("The password must be at least 6 characters long"))
        else Result.success(password)
    }

    /**
     * Валидация совпадения пароля
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     */
    fun validateConfirmPassword(password: String, confirmPassword: String): Result<String> {
        return if (password != confirmPassword)
            Result.failure(NotEqualPasswordException("Passwords do not match"))
        else Result.success(password)
    }

    /**
     * Валидация возраста
     */
    fun validateAge(age: Int?): Result<Int>{
        return if(age !in 5..100)
            Result.failure(InvalidAgeException("Age must be between 5 and 100"))
        else Result.success(age!!)
    }
    /**
     * Валидация веса
     */
    fun validateWeight(weight: Int?): Result<Int>{
        return if(weight!in 10..1000)
            Result.failure(InvalidWeightException("Weight must be between 10 and 1000"))
        else Result.success(weight!!)
    }
}