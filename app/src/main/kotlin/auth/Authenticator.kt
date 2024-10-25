package auth

/**
 * Интерфейс объекта для отправки запросов аутентификации на сервер
 */
interface Authenticator {
    class ServerConnectionException(message: String): Exception(message)
    class InvalidCredentialsException(message: String) : Exception(message)
    class UserAlreadyExistsException(message: String) : Exception(message)

    suspend fun register(name: String, login: String, password: String): Result<String>
    suspend fun login(login: String, password: String): Result<String>
}
