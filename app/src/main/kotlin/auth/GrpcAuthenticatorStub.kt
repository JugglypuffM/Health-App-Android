package auth

import domain.Either
import domain.User
import kotlinx.coroutines.delay

/**
 * Реализация заглушки для gRPC-сервиса аутентификации
 */
class GrpcAuthenticatorStub {
    suspend fun register(name: String, login: String, password: String): Either<Throwable, User> {
        delay(2000)
        return Either.Left(Authenticator.InvalidCredentialsException("Invalid credentials"))
    }
    suspend fun login(login: String, password: String): Either<Throwable, User> {
        delay(2000)
        return Either.Left(Authenticator.InvalidCredentialsException("Invalid credentials"))
    }
}