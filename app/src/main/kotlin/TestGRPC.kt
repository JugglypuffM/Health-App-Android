import auth.Authenticator
import auth.GrpcAuthenticator
import data.DataRequester
import data.GrpcDataRequester

suspend fun main() {
    val authenticator: Authenticator = GrpcAuthenticator()
    val dataRequester: DataRequester = GrpcDataRequester()
    println(authenticator.register("stas", "jpf", "123456"))
    println(authenticator.register("stas", "jpf", "123465"))
    println(authenticator.login("jpf", "123456"))
    println(authenticator.register("stas", "jpf", "1234"))
    println(authenticator.login("jpf", "1234"))
    println(dataRequester.getBasicUserData("jpf", "12345"))
    println(dataRequester.getBasicUserData("jpf", "123456"))
}