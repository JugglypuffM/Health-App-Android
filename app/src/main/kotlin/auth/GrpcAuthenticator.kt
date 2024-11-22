package auth

import async.AsyncCallExecutor
import auth.Authenticator.InvalidCredentialsException
import auth.Authenticator.UserAlreadyExistsException
import grpc.AuthProto.AuthResponse
import grpc.AuthProto.LoginRequest
import grpc.AuthProto.RegisterRequest
import grpc.AuthServiceGrpc
import grpc.AuthServiceGrpc.AuthServiceBlockingStub
import io.grpc.ManagedChannelBuilder

/**
 * Реализация интерфейса [Authenticator] с использованием gRPC
 * @param stub необязательный параметр gRPC-stub, по умолчанию сервер
 *             создает stub на канале по адресу SERVER_ADDRESS:SERVER_PORT из файла .env
 */
class GrpcAuthenticator(
    private val stub: AuthServiceBlockingStub = AuthServiceGrpc.newBlockingStub(
        ManagedChannelBuilder.forAddress(
            "192.168.1.74", 50051
        ).usePlaintext().build()
    )
) : Authenticator, AsyncCallExecutor {


    override suspend fun register(name: String, login: String, password: String): Result<String> =
        executeCallAsync(::processGrpcResponse) {
            val request =
                RegisterRequest.newBuilder().setLogin(login).setPassword(password)
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