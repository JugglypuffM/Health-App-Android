package KotlinAndroidApp

import android.app.Application
import android.util.Log
import auth.AuthenticatorStub
import data.DataRequesterStub
import utils.UserSerializer
import utils.Validator
import viewmodel.ViewModel
import domain.training.Training
import domain.training.Training.Yoga
import domain.training.Training.Jogging
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.minutes

/**
 * Класс, представляющий глобальное состояние приложения.
 */
class MainApplication : Application() {
    lateinit var viewModel: ViewModel
        private set

    val trainings: List<Training> = listOf(
        Yoga(LocalDate(2023, 10, 1), 60.minutes),
        Jogging(LocalDate(2023, 10, 2), 30.minutes, 5.0)
    )

    override fun onCreate() {
        super.onCreate()

        Log.d("ATH", "Application started")
        viewModel = ViewModel(
            UserSerializer(applicationContext),
            AuthenticatorStub(),
            DataRequesterStub(),
            Validator()
        )
    }
}