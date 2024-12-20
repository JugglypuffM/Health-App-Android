package services.data

import async.AsyncCallExecutor
import auth.Authenticator
import domain.BasicUserData
import grpc.DataProto.BasicDataRequest
import grpc.DataProto.BasicDataResponse
import grpc.DataServiceGrpc
import grpc.DataServiceGrpc.DataServiceBlockingStub
import io.github.cdimascio.dotenv.dotenv
import io.grpc.ManagedChannelBuilder

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
            val request =
                UserDataRequest.newBuilder().setLogin(login).setPassword(password).build()
            stub.getUserData(request)
            },
            ::processGrpcResponse
        )

    private fun processGrpcResponse(response: UserDataResponse): Result<UserInfo> =
        when (response.success) {
            true -> Result.success(
                UserInfo(
                    response.data.name,
                    response.data.age,
                    response.data.weight,
                    response.data.totalDistance
                )
            )

            false ->
                Result.failure(
                    Exceptions.InvalidCredentialsException(
                        "Failed to login user with provided credentials"
                    )
                )
        }
}