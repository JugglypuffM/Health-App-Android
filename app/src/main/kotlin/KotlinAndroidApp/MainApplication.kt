package KotlinAndroidApp

import android.app.Application
import android.util.Log
import services.auth.AuthenticatorService
import services.auth.GrpcAuthenticatorService
import com.project.kotlin_android_app.BuildConfig
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

        Log.d("ATH", "server url: ${BuildConfig.serverAddress}, port: ${BuildConfig.serverPort}")

        viewModel = ViewModel(
            UserSerializer(applicationContext),
            GrpcAuthenticatorService(
                AuthenticatorService.createAuthServiceBlockingStub(
                    BuildConfig.serverAddress,
                    BuildConfig.serverPort.toInt()
                )
            ),
            GrpcDataService(
                AuthenticatorService.createDataServiceBlockingStub(
                    BuildConfig.serverAddress,
                    BuildConfig.serverPort.toInt()
                )
            ),
            Validator()
        )
    }
}