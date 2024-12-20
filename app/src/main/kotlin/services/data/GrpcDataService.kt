package services.data

import domain.BasicUserData
import domain.exceptions.Exceptions
import grpc.DataProto.BasicDataRequest
import grpc.DataProto.BasicDataResponse
import grpc.DataServiceGrpc.DataServiceBlockingStub
import services.async.AsyncCallExecutor

class GrpcDataService(private val stub: DataServiceBlockingStub) : DataService,
    AsyncCallExecutor {
    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> =
        executeCallAsync(
            {
            val request =
                BasicDataRequest.newBuilder().setLogin(login).setPassword(password).build()
            stub.getBasicUserData(request)
            },
            ::processGrpcResponse
        )

    private fun processGrpcResponse(response: BasicDataResponse): Result<BasicUserData> =
        when (response.success) {
            true -> Result.success(BasicUserData(response.name))
            false ->
                Result.failure(
                    Exceptions.InvalidCredentialsException(
                        "Failed to login user with provided credentials"
                    )
                )
        }
}