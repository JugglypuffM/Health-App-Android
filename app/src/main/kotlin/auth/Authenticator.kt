package auth

import grpc.AuthProto.AuthResponse
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Интерфейс объекта для отправки запросов аутентификации на сервер
 */
interface Authenticator {
    class ServerConnectionException(message: String): Exception(message)
    class InvalidCredentialsException(message: String) : Exception(message)
    class UserAlreadyExistsException(message: String) : Exception(message)

    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String): Result<String>

    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Result<String>

    // Вспомогательная функция для выполнения gRPC вызовов с обработкой ошибок
    suspend fun executeGrpcCall(call: () -> AuthResponse): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                when (response.resultCode) {
                    0 -> Result.success(response.message)
                    1 -> Result.failure(Authenticator.UserAlreadyExistsException(response.message))
                    2 -> Result.failure(Authenticator.InvalidCredentialsException(response.message))
                    else -> Result.failure(Exception(response.message))
                }
            } catch (e: StatusRuntimeException) {
                Result.failure(
                    Authenticator.ServerConnectionException(
                        "Failed to connect to the server: server is unavailable"
                    )
                )
            }
        }
}
