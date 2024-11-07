package KotlinAndroidApp

import android.app.Application
import android.util.Log
import auth.Authenticator
import auth.GrpcAuthenticator
import com.project.kotlin_android_app.BuildConfig
import data.GrpcDataRequester
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
            GrpcAuthenticator(
                Authenticator.createAuthServiceBlockingStub(
                    BuildConfig.serverAddress,
                    BuildConfig.serverPort.toInt()
                )
            ),
            GrpcDataRequester(
                Authenticator.createDataServiceBlockingStub(
                    BuildConfig.serverAddress,
                    BuildConfig.serverPort.toInt()
                )
            ),
            Validator()
        )
    }
}