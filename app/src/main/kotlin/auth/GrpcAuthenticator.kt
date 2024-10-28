package auth

import domain.Either
import domain.User
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
    override suspend fun register(name: String, login: String, password: String): Either<Throwable, User> =
        executeGrpcCall(User(name, login, password)){
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
    override suspend fun login(login: String, password: String): Either<Throwable, User> = executeGrpcCall(User(null, login, password)) {
        val request = LoginRequest.newBuilder().setLogin(login).setPassword(password).build()
        stub.login(request)
    }

    // Вспомогательная функция для выполнения gRPC вызовов с обработкой ошибок
    private suspend fun executeGrpcCall(user: User, call: () -> AuthResponse): Either<Throwable, User> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                when (response.resultCode) {
                    0 -> Either.Right(user)
                    1 -> Either.Left(Authenticator.UserAlreadyExistsException(response.message))
                    2 -> Either.Left(Authenticator.InvalidCredentialsException(response.message))
                    else -> Either.Left(Exception(response.message))
                }
            } catch (e: StatusRuntimeException) {
                Either.Left(
                    Authenticator.ServerConnectionException(
                        "Failed to connect to the server: server is unavailable"
                    )
                )
            }
        }
}