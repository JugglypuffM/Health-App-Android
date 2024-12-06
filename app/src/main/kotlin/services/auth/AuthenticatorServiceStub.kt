package services.auth

import kotlinx.coroutines.delay

/**
 * Реализация заглушки для gRPC-сервиса аутентификации
 */
class AuthenticatorServiceStub : AuthenticatorService {
    override suspend fun register(name: String, login: String, password: String): Result<String> {
        delay(2000)
        return Result.success("success server register")
    }
    override suspend fun login(login: String, password: String): Result<String> {
        delay(2000)
        return Result.success("success server login")
    }
}