package KotlinAndroidApp

import android.app.Application
import auth.Authenticator
import auth.GrpcAuthenticator
import config.Config
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
        val config = Config(applicationContext.resources)
        viewModel = ViewModel(
            UserSerializer(applicationContext),
            GrpcAuthenticator(
                Authenticator.createAuthServiceBlockingStub(
                    config.serverConfig.url,
                    config.serverConfig.port
                )
            ),
            GrpcDataRequester(
                Authenticator.createDataServiceBlockingStub(
                    config.serverConfig.url,
                    config.serverConfig.port
                )
            ),
            Validator()
        )
    }
}