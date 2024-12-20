package KotlinAndroidApp

import android.app.Application
import android.util.Log
import com.project.kotlin_android_app.BuildConfig
import services.auth.GrpcAuthenticatorService
import services.data.GrpcDataService
import utils.UserSerializer
import utils.Validator
import viewmodel.ViewModel

/**
 * Класс, представляющий глобальное состояние приложения.
 */
class MainApplication : Application() {
    lateinit var viewModel: ViewModel
        private set

    override fun onCreate() {
        super.onCreate()

        Log.d("ATH", "server address: ${BuildConfig.serverAddress}, port: ${BuildConfig.serverPort}")
        viewModel = ViewModel(
            UserSerializer(applicationContext),
            GrpcAuthenticatorService(
                    BuildConfig.serverAddress,
                    BuildConfig.serverPort.toInt()
            ),
            GrpcDataService(
                    BuildConfig.serverAddress,
                    BuildConfig.serverPort.toInt()
            ),
            Validator()
        )
    }
}