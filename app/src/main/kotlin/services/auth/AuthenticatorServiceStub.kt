package services.auth


/**
 * Реализация заглушки для gRPC-сервиса аутентификации
 */
class AuthenticatorServiceStub : AuthenticatorService {
    override suspend fun register(login: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }
    override suspend fun login(login: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }
}