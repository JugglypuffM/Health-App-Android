package data

import domain.BasicUserData
import grpc.DataProto.BasicDataRequest
import grpc.DataServiceGrpc
import grpc.DataServiceGrpc.DataServiceBlockingStub
import io.github.cdimascio.dotenv.dotenv
import io.grpc.ManagedChannelBuilder

class GrpcDataRequester(
    private val stub: DataServiceBlockingStub = DataServiceGrpc.newBlockingStub(
        ManagedChannelBuilder.forAddress(
            dotenv()["SERVER_ADDRESS"], dotenv()["SERVER_PORT"].toInt()
        ).usePlaintext().build()
    )
) : DataRequester{
    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> = executeCallAsync {
        val request = BasicDataRequest.newBuilder().setLogin(login).setPassword(password).build()
        stub.getBasicUserData(request)
    }
}