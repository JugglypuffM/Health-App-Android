package utils

import domain.User
import domain.flatMap

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
            Result.failure(Validator.InvalidNameException("User name is empty"))
        else Result.success(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    private fun validateLogin(login: String): Result<String> {
        return if (login.isBlank())
            Result.failure(Validator.InvalidLoginException("Login is empty"))
        else Result.success(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    private fun validatePassword(password: String): Result<String> {
        return if (password.length < 6)
            Result.failure(Validator.InvalidPasswordException("The password must be at least 6 characters long"))
        else Result.success(password)
    }

    /**
     * Валидация совпадения пароля
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     */
    private fun validateConfirmPassword(password: String, confirmPassword: String): Result<String> {
        return if (password != confirmPassword)
            Result.failure(Validator.NotEqualPasswordException("Passwords do not match"))
        else Result.success(password)
    }

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    fun check(login: String, password: String): Result<User> {
        return validateLogin(login).flatMap { _ ->
            validatePassword(password).map {_ ->
                User(null, login, password)
            }
        }
    }

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    fun check(name: String, login: String, password: String, confirmPassword: String): Result<User> {
        return validateName(name).flatMap {_ ->
            validateLogin(login).flatMap { _ ->
                validateConfirmPassword(password, confirmPassword).flatMap { _ ->
                    validatePassword(password).map {_ ->
                        User(name, login, password)
                    }
                }
            }
        }
    }
}