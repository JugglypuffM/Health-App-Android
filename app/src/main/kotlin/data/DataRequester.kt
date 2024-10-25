package data

import auth.Authenticator
import domain.BasicUserData
import grpc.DataProto.BasicDataResponse
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Интерфейс объекта для запроса данных пользователя с сервера
 */
interface DataRequester {

    /**
     * Функция запроса основной информации о пользователе
     * @param login логин пользователя
     * @param password пароль пользователя
     */
    suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData>

    // Вспомогательная функция для выполнения gRPC вызовов с обработкой ошибок
    suspend fun executeCallAsync(call: () -> BasicDataResponse): Result<BasicUserData> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                when (response.success) {
                    true -> Result.success(BasicUserData(response.name))
                    false ->
                        Result.failure(
                            Authenticator
                                .InvalidCredentialsException(
                                    "Failed to login user with provided credentials"
                                )
                        )
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