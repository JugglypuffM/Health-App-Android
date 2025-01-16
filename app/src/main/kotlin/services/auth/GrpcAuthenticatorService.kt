package services.auth

import com.google.protobuf.Empty
import domain.exceptions.Exceptions
import grpc.AuthProto.AuthRequest
import grpc.AuthServiceGrpc
import grpc.AuthServiceGrpc.AuthServiceBlockingStub
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import services.grpc.AsyncGrpcService

/**
 * Реализация интерфейса [AuthenticatorService] с использованием gRPC
 * @param stub обязательный параметр gRPC-stub
 */
class GrpcAuthenticatorService(private val stub: AuthServiceBlockingStub) : AuthenticatorService,
    AsyncGrpcService {
    /**
     * @param address Адреса сервера
     * @param port Порт сервера
     */
    constructor(address: String, port: Int) :
            this(
                AuthServiceGrpc.newBlockingStub(
                    ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()
                )
            )

    override suspend fun register(login: String, password: String): Result<Unit> =
        executeCallAsyncWithError<Empty, Unit>(
            {
                val request =
                    AuthRequest.newBuilder().setLogin(login).setPassword(password).build()
                stub.register(request)
            },
            {
                Result.success(Unit)
            },
            ::processGrpcError
        )

    override suspend fun login(login: String, password: String): Result<Unit> =
        executeCallAsyncWithError<Empty, Unit>(
            {
                val request = AuthRequest.newBuilder().setLogin(login).setPassword(password).build()
                stub.login(request)
            },
            { Result.success(Unit) },
            ::processGrpcError
        )

    private fun processGrpcError(error: Throwable): Result<Unit> =
        when (error) {
            is StatusRuntimeException -> {
                val status = Status.fromThrowable(error)

                when (status.code) {
                    Status.Code.UNAVAILABLE -> Result.failure(
                        Exceptions.ServerConnectionException(
                            error.message.orEmpty()
                        )
                    )

                    Status.Code.UNAUTHENTICATED ->
                        Result.failure(Exceptions.InvalidCredentialsException(error.message.orEmpty()))

                    Status.Code.INVALID_ARGUMENT ->
                        Result.failure(Exceptions.InvalidArgumentException(error.message.orEmpty()))

                    Status.Code.ALREADY_EXISTS ->
                        Result.failure(Exceptions.UserAlreadyExistsException(error.message.orEmpty()))

                    else ->
                        Result.failure(Exceptions.UnexpectedError(error.message.orEmpty()))
                }
            }

            else -> Result.failure(error)
        }
}