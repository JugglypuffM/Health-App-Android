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
            //TODO добавить обработку регистрации состояний клиента:
            // не зарегистрирован - стабы пустые, после регистрации они обновляются,
            // при повторном запуске приложения они сразу строятся по данным из памяти
            GrpcDataService(
                "", // это, очевидно, надо убрать
                "", // это, очевидно, тоже))
                // а точнее поменять на логику обработки случаев если клиент авторизован или нет
                    BuildConfig.serverAddress,
                    BuildConfig.serverPort.toInt()
            ),
            Validator()
        )
    }
}