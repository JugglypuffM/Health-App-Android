import auth.Authenticator
import auth.GrpcAuthenticator
import config.Config
import data.DataRequester
import data.GrpcDataRequester

suspend fun main() {
    val url = "https://localhost"
    val port = 228

    val authenticator: Authenticator = GrpcAuthenticator(Authenticator.createAuthServiceBlockingStub(url, port))
    val dataRequester: DataRequester = GrpcDataRequester(Authenticator.createDataServiceBlockingStub(url, port))

    println(authenticator.register("stas", "jpf", "123456"))
    println(authenticator.register("stas", "jpf", "123465"))
    println(authenticator.login("jpf", "123456"))
    println(authenticator.register("stas", "jpf", "1234"))
    println(authenticator.login("jpf", "1234"))
    println(dataRequester.getBasicUserData("jpf", "12345"))
    println(dataRequester.getBasicUserData("jpf", "123456"))
}