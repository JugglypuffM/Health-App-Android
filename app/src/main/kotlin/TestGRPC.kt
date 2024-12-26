import domain.UserInfo
import domain.training.Training
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import services.auth.AuthenticatorService
import services.auth.GrpcAuthenticatorService
import services.data.DataService
import services.data.GrpcDataService
import services.training.GrpcTrainingService
import services.training.TrainingService
import kotlin.time.Duration.Companion.minutes

//TODO: Перетащить в не запускающиеся автоматически тесты
suspend fun main() {
    val address = "localhost"
    val port = 50051

    val authenticatorService: AuthenticatorService = GrpcAuthenticatorService(address, port)
    val dataService: DataService = GrpcDataService("jpf", "123456", address, port)
    val badDataService: DataService = GrpcDataService("not-jpf", "654321", address, port)
    val trainingService: TrainingService = GrpcTrainingService("jpf", "123456", address, port)

    val date = LocalDate.fromEpochDays(Clock.System.now().epochSeconds.toInt())

    println("Удачная регистрация")
    println(authenticatorService.register("stas", "jpf", "123456"))
    println()

    println("Неудачная повторная регистрация - пользователь уже существует")
    println(authenticatorService.register("stas", "jpf", "123465"))
    println()

    println("Удачный логин")
    println(authenticatorService.login("jpf", "123456"))
    println()

    println("Неудачная регистрация после логина - пользователь уже существует")
    println(authenticatorService.register("stas", "jpf", "1234"))
    println()

    println("Неудачный логин - плохие креды")
    println(authenticatorService.login("jpf", "1234"))
    println()

    println("Удачный запрос пока что пустых данных пользователя")
    println(dataService.getUserData())
    println()

    println("Неудачный запрос ... - плохие креды")
    println(badDataService.getUserData())
    println()

    println("Удачное обновление данных")
    println(dataService.updateUserData(UserInfo("sas", 20, 85, 5)))
    println()

    println("Неудачное обновление данных - плохие креды")
    println(
        badDataService.updateUserData(
            UserInfo("ne-sas", 200, 85000000, 500000000)
        )
    )
    println()

    println("Удачный запрос новых, полных данных пользователя")
    println(dataService.getUserData())
    println()

    println("Удачное сохранение тренировки")
    println(
        trainingService.saveTraining(
            Training.Jogging(
                date,
                10.minutes,
                1000.0
            )
        )
    )
    println()

    println("Удачный запрос данных с изменившейся дистанцией")
    println(dataService.getUserData())
    println()

    println("Удачный запрос тренировок на дату")
    println(trainingService.getTrainings(date))
    println()


}