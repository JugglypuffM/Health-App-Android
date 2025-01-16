package services.data


import com.google.protobuf.Empty
import domain.UserInfo
import domain.exceptions.Exceptions
import grpc.DataServiceGrpc
import grpc.DataServiceGrpc.DataServiceBlockingStub
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.MetadataUtils
import services.grpc.AsyncGrpcService

class GrpcDataService(private val stub: DataServiceBlockingStub) : DataService,
    AsyncGrpcService {
    /**
     * @param address Адреса сервера
     * @param port Порт сервера
     */
    constructor(login: String, password: String, address: String, port: Int) : this(
        DataServiceGrpc.newBlockingStub(
            ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()
        ).withInterceptors(
            MetadataUtils.newAttachHeadersInterceptor(
                AsyncGrpcService.createMetadata(login, password)
            )
        )
    )

    override suspend fun getUserData(): Result<UserInfo> =
        executeCallAsyncWithError(
            { stub.getUserData(Empty.getDefaultInstance()) },
            { Result.success(UserInfo(it)) },
            {
                when (it) {
                    is StatusRuntimeException -> {
                        val status = Status.fromThrowable(it)

                        when (status.code) {
                            Status.Code.UNAVAILABLE -> Result.failure(
                                Exceptions.ServerConnectionException(
                                    it.message.orEmpty()
                                )
                            )
                            Status.Code.UNAUTHENTICATED -> Result.failure(
                                Exceptions.InvalidCredentialsException(
                                    "Invalid credentials passed"
                                )
                            )

                            else -> Result.failure(
                                Exceptions.UnexpectedError(it.message.orEmpty())
                            )
                        }
                    }

                    else -> Result.failure(it)
                }
            }
        )

    override suspend fun updateUserData(info: UserInfo): Result<Unit> =
        executeCallAsyncWithError<Empty, Unit>(
            { stub.updateUserData(info.toUserData()) },
            { Result.success(Unit) },
            {
                when (it) {
                    is StatusRuntimeException -> {
                        val status = Status.fromThrowable(it)

                        when (status.code) {
                            Status.Code.UNAVAILABLE -> Result.failure(
                                Exceptions.ServerConnectionException(
                                    it.message.orEmpty()
                                )
                            )

                            Status.Code.INVALID_ARGUMENT -> Result.failure(
                                Exceptions.InvalidArgumentException(
                                    "No data provided"
                                )
                            )

                            Status.Code.UNAUTHENTICATED -> Result.failure(
                                Exceptions.InvalidCredentialsException(
                                    "Invalid credentials passed"
                                )
                            )

                            else -> Result.failure(
                                Exceptions.UnexpectedError("Invalid server response ${status.code}")
                            )
                        }
                    }

                    else -> Result.failure(it)
                }
            }
        )
}