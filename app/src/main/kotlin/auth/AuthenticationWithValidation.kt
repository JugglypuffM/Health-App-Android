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
class AuthenticationWithValidation(private val authenticator: Authenticator): Authenticator{
    /**
     * Валидация имени
     * @param name имя пользователя
     */
    private fun invalidateName(name: String): Result<String> {
        if (name.isBlank()) {
            return Result.failure(Authenticator.InvalidCredentialsException("Имя пользователя пустое"))
        }

        return Result.success(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    private fun invalidateLogin(login: String): Result<String> {
        if(login.isBlank()){
            return Result.failure(Authenticator.InvalidCredentialsException("Логин пользователя пуст"))
        }

        return Result.success(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    private fun invalidatePassword(password: String): Result<String> {
        if (password.length < 6) {
            return Result.failure(Authenticator.InvalidCredentialsException("Пароль должен быть не менее 6 символов"))
        }

        return Result.success(password)
    }

    /**
     * Функция для регистрации нового пользователя
     * Проверяет корректность входных параметров
     * @param name имя пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    override suspend fun register(name: String, login: String, password: String): Result<String> {
        return invalidateName(name).fold(
            onSuccess = { _ ->
                invalidateLogin(login).fold(
                    onSuccess = { _ ->
                        invalidatePassword(password).fold(
                            onSuccess = { return authenticator.register(name, login, password) },
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
    override suspend fun login(login: String, password: String): Result<String> {
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