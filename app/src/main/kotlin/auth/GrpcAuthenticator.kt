package auth

import async.AsyncCallExecutor
import auth.Authenticator.InvalidCredentialsException
import auth.Authenticator.UserAlreadyExistsException
import grpc.AuthProto.*
import grpc.AuthServiceGrpc.AuthServiceBlockingStub

/**
 * Реализация интерфейса [Authenticator] с использованием gRPC
 * @param stub обязательный параметр gRPC-stub
 */
class GrpcAuthenticator(private val stub: AuthServiceBlockingStub) : Authenticator, AsyncCallExecutor {
    override suspend fun register(name: String, login: String, password: String): Result<String> =
        executeCallAsync(::processGrpcResponse) {
            val request =
                RegisterRequest.newBuilder().setName(name).setLogin(login).setPassword(password)
                    .build()
            stub.register(request)
        }

    override suspend fun login(login: String, password: String): Result<String> =
        executeCallAsync(::processGrpcResponse) {
            val request = LoginRequest.newBuilder().setLogin(login).setPassword(password).build()
            stub.login(request)
        }

    private fun processGrpcResponse(response: AuthResponse): Result<String> =
        when (response.resultCode) {
            0 -> Result.success(response.message)
            1 -> Result.failure(UserAlreadyExistsException(response.message))
            2 -> Result.failure(InvalidCredentialsException(response.message))
            else -> Result.failure(Exception(response.message))
        }
}