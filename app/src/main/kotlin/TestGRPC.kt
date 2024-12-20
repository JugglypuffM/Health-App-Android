import services.auth.AuthenticatorService
import services.auth.GrpcAuthenticatorService
import services.data.DataService
import services.data.GrpcDataService

suspend fun main() {
    val address = "https://localhost"
    val port = 228

    val authenticatorService: AuthenticatorService = GrpcAuthenticatorService(AuthenticatorService.createAuthServiceBlockingStub(url, port))
    val dataService: DataService = GrpcDataService(AuthenticatorService.createDataServiceBlockingStub(url, port))

    println(authenticatorService.register("stas", "jpf", "123456"))
    println(authenticatorService.register("stas", "jpf", "123465"))
    println(authenticatorService.login("jpf", "123456"))
    println(authenticatorService.register("stas", "jpf", "1234"))
    println(authenticatorService.login("jpf", "1234"))
    println(dataService.getUserData("jpf", "12345"))
    println(dataService.getUserData("jpf", "123456"))
}