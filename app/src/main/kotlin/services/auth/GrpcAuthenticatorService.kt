package services.auth

import domain.exceptions.Exceptions
import grpc.AuthProto.AuthResponse
import grpc.AuthProto.LoginRequest
import grpc.AuthProto.RegisterRequest
import grpc.AuthServiceGrpc
import grpc.AuthServiceGrpc.AuthServiceBlockingStub
import io.grpc.ManagedChannelBuilder
import services.async.AsyncCallExecutor

/**
 * Реализация интерфейса [AuthenticatorService] с использованием gRPC
 * @param stub обязательный параметр gRPC-stub
 */
class GrpcAuthenticatorService(private val stub: AuthServiceBlockingStub) : AuthenticatorService,
    AsyncCallExecutor {
    /**
     * @param address Адреса сервера
     * @param port Порт сервера
     */
    constructor(address: String, port: Int):
        this(
            AuthServiceGrpc.newBlockingStub(
            ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()
        ))
    override suspend fun register(name: String, login: String, password: String): Result<String> =
        executeCallAsync(
            {
            val request =
                RegisterRequest.newBuilder().setLogin(login).setPassword(password)
                    .build()
            stub.register(request)
            },
            ::processGrpcResponse
        )

    override suspend fun login(login: String, password: String): Result<String> =
        executeCallAsync(
            {
            val request = LoginRequest.newBuilder().setLogin(login).setPassword(password).build()
            stub.login(request)
            },
            ::processGrpcResponse
        )

    private fun processGrpcResponse(response: AuthResponse): Result<String> =
        when (response.resultCode) {
            0 -> Result.success(response.message)
            1 -> Result.failure(Exceptions.UserAlreadyExistsException(response.message))
            2 -> Result.failure(Exceptions.InvalidCredentialsException(response.message))
            else -> Result.failure(Exception(response.message))
        }
}