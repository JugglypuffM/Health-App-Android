package auth

import kotlinx.coroutines.delay

class GrpcAuthenticatorStub: Authenticator {
    override suspend fun register(name: String, login: String, password: String): Result<String> {
        delay(2000)
        return Result.failure(Authenticator.UserAlreadyExistsException("User with the same login already exists"))
    }
    override suspend fun login(login: String, password: String): Result<String> {
        delay(2000)
        return Result.success("User authenticated successfully")
    }
}