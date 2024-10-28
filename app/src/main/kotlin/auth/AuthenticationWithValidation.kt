package auth

/**
 * Класс для валидации данных регистрации/авторизации
 * @param authenticator класс дальнейшая стадии регистрации/авторизации
 *
 * При успешных данных передаёт результат в authentication
 *
 * Валидация происходит по следующему правилу
 * Имя является не пустым
 * Логин является не пустым
 * Пароль содержит не менее 6и символов
 */
class AuthenticationWithValidation(private val authenticator: Authenticator) {
    class InvalidNameException(message: String) : Exception(message)
    class InvalidLoginException(message: String) : Exception(message)
    class NotEqualPasswordException(message: String) : Exception(message)
    class InvalidPasswordException(message: String) : Exception(message)

    /**
     * Валидация имени
     * @param name имя пользователя
     */
    private fun invalidateName(name: String): Result<String> {
        return if (name.isBlank())
            Result.failure(AuthenticationWithValidation.InvalidNameException("User name is empty"))
        else Result.success(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    private fun invalidateLogin(login: String): Result<String> {
        return if (login.isBlank())
            Result.failure(AuthenticationWithValidation.InvalidLoginException("Login is empty"))
        else Result.success(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    private fun invalidatePassword(password: String): Result<String> {
        return if (password.length < 6)
            Result.failure(AuthenticationWithValidation.InvalidPasswordException("The password must be at least 6 characters long"))
        else Result.success(password)
    }

    /**
     * Валидация совпадения пароля
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     */
    private fun invalidateConfirmPassword(password: String, confirmPassword: String): Result<String> {
        return if (password != confirmPassword)
            Result.failure(AuthenticationWithValidation.NotEqualPasswordException("Passwords do not match"))
        else Result.success(password)
    }

    /**
     * Функция для регистрации нового пользователя
     * Проверяет корректность входных параметров
     * @param name имя пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String, confirmPassword: String): Result<String> {
        return invalidateName(name).fold(
            onSuccess = { _ ->
                invalidateLogin(login).fold(
                    onSuccess = { _ ->
                        invalidateConfirmPassword(password, confirmPassword).fold(
                            onSuccess = {
                                invalidatePassword(password).fold(
                                    onSuccess = {
                                        return authenticator.register(name, login, password)
                                    },
                                    onFailure = { error ->
                                        Result.failure(error)
                                    }
                                )
                            },
                            onFailure = { error -> Result.failure(error) }
                        )
                    },
                    onFailure = { error -> Result.failure(error) }
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }

    /**
     * Функция авторизации пользователя
     * Проверяет корректность входных параметров
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Result<String> {
        return invalidateLogin(login).fold(
            onSuccess = { _ ->
                invalidatePassword(password).fold(
                    onSuccess = { return authenticator.login(login, password) },
                    onFailure = { error -> Result.failure(error) }
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}