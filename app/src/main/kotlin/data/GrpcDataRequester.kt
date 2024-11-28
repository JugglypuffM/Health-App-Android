package data

import async.AsyncCallExecutor
import auth.Authenticator
import domain.UserInfo
import grpc.DataProto.UserDataRequest
import grpc.DataProto.UserDataResponse
import grpc.DataServiceGrpc
import grpc.DataServiceGrpc.DataServiceBlockingStub
import io.grpc.ManagedChannelBuilder

class GrpcDataRequester(private val stub: DataServiceBlockingStub) : DataRequester, AsyncCallExecutor {
    constructor(address: String, port: Int): this(DataServiceGrpc.newBlockingStub(
        ManagedChannelBuilder.forAddress(address, port).usePlaintext().build()
    ))

    override suspend fun getUserData(login: String, password: String): Result<UserInfo> =
        executeCallAsync(::processGrpcResponse) {
            val request =
                UserDataRequest.newBuilder().setLogin(login).setPassword(password).build()
            stub.getUserData(request)
        }

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
                    Authenticator.InvalidCredentialsException(
                        "Failed to login user with provided credentials"
                    )
                )
        }
}