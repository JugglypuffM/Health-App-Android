import services.auth.AuthenticatorService
import services.auth.GrpcAuthenticatorService
import services.data.DataService
import services.data.GrpcDataService

suspend fun main() {
    val address = "localhost"
    val port = 228

    val authenticatorService: AuthenticatorService = GrpcAuthenticatorService(address, port)
    val dataService: DataService = GrpcDataService(address, port)

    println(authenticatorService.register("stas", "jpf", "123456"))
    println(authenticatorService.register("stas", "jpf", "123465"))
    println(authenticatorService.login("jpf", "123456"))
    println(authenticatorService.register("stas", "jpf", "1234"))
    println(authenticatorService.login("jpf", "1234"))
    println(dataService.getUserData("jpf", "12345"))
    println(dataService.getUserData("jpf", "123456"))
}