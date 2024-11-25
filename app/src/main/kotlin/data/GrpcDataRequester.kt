package data

import async.AsyncCallExecutor
import auth.Authenticator
import domain.BasicUserData
import grpc.DataProto.BasicDataRequest
import grpc.DataProto.BasicDataResponse
import grpc.DataServiceGrpc
import grpc.DataServiceGrpc.DataServiceBlockingStub
import io.github.cdimascio.dotenv.dotenv
import io.grpc.ManagedChannelBuilder

class GrpcDataRequester(private val stub: DataServiceBlockingStub) : DataRequester, AsyncCallExecutor {
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
                    Authenticator.InvalidCredentialsException(
                        "Failed to login user with provided credentials"
                    )
                )
        }
}