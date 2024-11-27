package data

//import async.AsyncCallExecutor
//import auth.Authenticator
//import domain.BasicUserData
//import grpc.DataProto
//import grpc.DataServiceGrpc
//import grpc.DataServiceGrpc.DataServiceBlockingStub
//import io.grpc.ManagedChannelBuilder

//@Deprecated("Use new GrpcDataRequester")
//class OldGrpcDataRequester(
//    private val stub: DataServiceBlockingStub = DataServiceGrpc.newBlockingStub(
//        ManagedChannelBuilder.forAddress(
//            "192.168.1.74", 50051
//        ).usePlaintext().build()
//    )
//) : OldDataRequester, AsyncCallExecutor {
//    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> =
//        executeCallAsync(::processGrpcResponse) {
//            val request =
//                DataProto.UserDataRequest.newBuilder().setLogin(login).setPassword(password).build()
//            stub.getUserData(request)
//        }
//
//    private fun processGrpcResponse(response: DataProto.UserDataResponse): Result<BasicUserData> =
//        when (response.success) {
//            true -> Result.success(BasicUserData(response.data.name))
//            false ->
//                Result.failure(
//                    Authenticator.InvalidCredentialsException(
//                        "Failed to login user with provided credentials"
//                    )
//                )
//        }
//}