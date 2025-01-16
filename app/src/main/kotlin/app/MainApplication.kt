package app

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.project.kotlin_android_app.BuildConfig
import domain.Account
import domain.User
import domain.training.TrainingHistory
import services.auth.AuthenticatorService
import services.auth.GrpcAuthenticatorService
import services.data.DataService
import services.data.GrpcDataService
import services.training.GrpcTrainingService
import services.training.TrainingService
import utils.CustomLogger
import utils.UserSerializer
import utils.Validator
import utils.XMLReader

/**
 * Класс, представляющий глобальное состояние приложения.
 */
class MainApplication : Application() {
    val logger: CustomLogger = CustomLogger()
    lateinit var xmlReader: XMLReader
        private set

    val authenticator: AuthenticatorService = GrpcAuthenticatorService(
        BuildConfig.serverAddress,
        BuildConfig.serverPort.toInt()
    )
    var dataRequester: DataService? = null
        private set

    var trainingService: TrainingService? = null
        private set

    var user = User.empty()
    val validator = Validator()
    lateinit var userSerializer: UserSerializer
        private set

    val trainingHistory = MutableLiveData(TrainingHistory(emptyList()))

    override fun onCreate() {
        super.onCreate()
        xmlReader = XMLReader(this)
        userSerializer = UserSerializer(this)
    }

    fun createServices(account: Account): Pair<DataService, TrainingService> {
        dataRequester = GrpcDataService(
            account.login,
            account.password,
            BuildConfig.serverAddress,
            BuildConfig.serverPort.toInt()
        )

        trainingService = GrpcTrainingService(
            account.login,
            account.password,
            BuildConfig.serverAddress,
            BuildConfig.serverPort.toInt()
        )

        return Pair(dataRequester!!, trainingService!!)
    }

//    fun createServices(account: Account): Pair<DataService, TrainingService>{
//        dataRequester = DataServiceStub()
//        trainingService = TrainingServiceStub()
//        return Pair(dataRequester!!, trainingService!!)
//    }
}