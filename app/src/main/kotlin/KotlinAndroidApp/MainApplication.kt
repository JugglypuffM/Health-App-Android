package KotlinAndroidApp

import android.app.Application
import com.project.kotlin_android_app.BuildConfig
import domain.Account
import domain.User
import domain.training.Training
import domain.training.TrainingActions
import domain.training.TrainingHistory
import kotlinx.datetime.LocalDate
import services.auth.AuthenticatorService
import services.auth.GrpcAuthenticatorService
import services.data.DataService
import services.data.GrpcDataService
import utils.UserSerializer
import utils.Validator
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Класс, представляющий глобальное состояние приложения.
 */
class MainApplication : Application() {
    val authenticator: AuthenticatorService = GrpcAuthenticatorService(
        BuildConfig.serverAddress,
        BuildConfig.serverPort.toInt()
    )
    var dataRequester: DataService? = null
    private set

    var user = User.empty()
    val validator = Validator()
    lateinit var userSerializer: UserSerializer
    private set

    var currentTraining: TrainingActions? = null
    val trainingHistory: TrainingHistory = TrainingHistory(mutableListOf())

    override fun onCreate() {
        super.onCreate()
        userSerializer = UserSerializer(applicationContext)
    }

    fun createDataRequester(account: Account){
        dataRequester = GrpcDataService(
            account.login,
            account.password,
            BuildConfig.serverAddress,
            BuildConfig.serverPort.toInt()
        )
    }
}