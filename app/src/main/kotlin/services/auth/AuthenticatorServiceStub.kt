package services.auth

import kotlinx.coroutines.delay

/**
 * Реализация заглушки для gRPC-сервиса аутентификации
 */
class AuthenticatorServiceStub : AuthenticatorService {
    override suspend fun register(name: String, login: String, password: String): Result<Unit> {
        delay(2000)
        return Result.success(Unit)
    }
    override suspend fun login(login: String, password: String): Result<Unit> {
        delay(2000)
        return Result.success(Unit)
    }
}