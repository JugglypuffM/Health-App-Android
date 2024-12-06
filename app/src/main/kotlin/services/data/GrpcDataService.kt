package services.data

import services.async.AsyncCallExecutor
import services.auth.AuthenticatorService
import domain.BasicUserData
import grpc.DataProto.BasicDataRequest
import grpc.DataProto.BasicDataResponse
import grpc.DataServiceGrpc.DataServiceBlockingStub

class GrpcDataService(private val stub: DataServiceBlockingStub) : DataService,
    AsyncCallExecutor {
    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> =
        executeCallAsync(::processGrpcResponse) {
            val request =
                BasicDataRequest.newBuilder().setLogin(login).setPassword(password).build()
            stub.getBasicUserData(request)
        }

    private fun processGrpcResponse(response: BasicDataResponse): Result<BasicUserData> =
        when (response.success) {
            true -> Result.success(BasicUserData(response.name))
            false ->
                Result.failure(
                    AuthenticatorService.InvalidCredentialsException(
                        "Failed to login user with provided credentials"
                    )
                )
        }
}