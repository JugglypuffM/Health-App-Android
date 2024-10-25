package auth

import io.github.cdimascio.dotenv.dotenv
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.grpc.AuthProto.*
import org.example.grpc.AuthServiceGrpc
import org.example.grpc.AuthServiceGrpc.AuthServiceBlockingStub

/**
 * Реализация интерфейса [Authenticator] с использованием gRPC
 * @param stub необязательный параметр gRPC-stub, по умолчанию сервер
 *             создает stub на канале по адресу SERVER_ADDRESS:SERVER_PORT из файла .env
 */
class GrpcAuthenticator(
    private val stub: AuthServiceBlockingStub = AuthServiceGrpc.newBlockingStub(
        ManagedChannelBuilder.forAddress(
            dotenv()["SERVER_ADDRESS"], dotenv()["SERVER_PORT"].toInt()
        ).usePlaintext().build()
    )
) : Authenticator {

    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    override suspend fun register(name: String, login: String, password: String): Result<String> =
        executeGrpcCall {
            val request =
                RegisterRequest.newBuilder().setName(name).setLogin(login).setPassword(password)
                    .build()
            stub.register(request)
        }

    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    override suspend fun login(login: String, password: String): Result<String> = executeGrpcCall {
        val request = LoginRequest.newBuilder().setLogin(login).setPassword(password).build()
        stub.login(request)
    }

    // Вспомогательная функция для выполнения gRPC вызовов с обработкой ошибок
    private suspend fun executeGrpcCall(call: () -> AuthResponse): Result<String> =
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