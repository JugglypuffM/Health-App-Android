package KotlinAndroidApp

import android.app.Application
import android.util.Log
import auth.Authenticator
import auth.AuthenticatorStub
import auth.GrpcAuthenticator
import com.project.kotlin_android_app.BuildConfig
import data.DataRequesterStub
import data.GrpcDataRequester
import domain.Account
import domain.User
import domain.UserInfo
import domain.training.Training
import domain.training.TrainingHistory
import kotlinx.datetime.LocalDate
import utils.UserSerializer
import utils.Validator
import viewmodel.ViewModel
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Класс, представляющий глобальное состояние приложения.
 */
class MainApplication : Application() {
    lateinit var viewModel: ViewModel
        private set

    var user = User(Account("stubLogin", "stubPassword"), UserInfo("stubName", 0, 0, 0))

    val trainingHistory: TrainingHistory = TrainingHistory(listOf(
        Training.Yoga(
            date = LocalDate(2024, 12, 20),
            duration = 60.minutes
        ),
        Training.Yoga(
            date = LocalDate(2024, 12, 19),
            duration = 60.minutes
        ),
        Training.Yoga(
            date = LocalDate(2024, 12, 18),
            duration = 60.minutes
        ),
        Training.Jogging(
            date = LocalDate(2024, 12, 17),
            duration = 45.seconds,
            distance = 5.0
        )
    ))

    override fun onCreate() {
        super.onCreate()

        Log.d("ATH", "server address: ${BuildConfig.serverAddress}, port: ${BuildConfig.serverPort}")

        viewModel = ViewModel(
            UserSerializer(applicationContext),
            AuthenticatorStub(),
            DataRequesterStub(),
            Validator()
        )
    }
}