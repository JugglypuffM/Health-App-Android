package services.data


import com.google.protobuf.Empty
import domain.UserInfo
import domain.exceptions.Exceptions
import grpc.DataProto
import grpc.DataProto.UpdateDataRequest
import grpc.DataServiceGrpc
import grpc.DataServiceGrpc.DataServiceBlockingStub
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import services.async.AsyncCallExecutor

class GrpcDataService(private val stub: DataServiceBlockingStub) : DataService,
    AsyncCallExecutor {
    /**
     * @param address Адреса сервера
     * @param port Порт сервера
     */
    constructor(address: String, port: Int): this(DataServiceGrpc.newBlockingStub(
        ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()
    ))

    override suspend fun getUserData(login: String, password: String): Result<UserInfo> =
        executeCallAsync(
            {
            val request = DataProto.UserDataRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build()

            stub.getUserData(request)
            },
            ::processGrpcResponse
        )

    override suspend fun updateUserData(login: String, password: String, info: UserInfo): Result<Unit> =
        executeCallAsyncWithError<Empty, Unit>(
            {
                val request = UpdateDataRequest.newBuilder()
                    .setLogin(login)
                    .setPassword(password)
                    .setData(info.toUserData())
                    .build()

                stub.updateUserData(request)
            },
            {
                Result.success(Unit)
            },
            {
                when (it) {
                    is StatusRuntimeException -> {
                        val status = Status.fromThrowable(it)

                        when (status.code) {
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

    private fun processGrpcResponse(response: DataProto.UserDataResponse): Result<UserInfo> =
        when (response.success) {
            true -> Result.success(UserInfo(response.data))

            false ->
                Result.failure(
                    Exceptions.UnexpectedError(
                        "Something went wrong: it may be invalid credentials or just some bug"
                    )
                )
        }
}