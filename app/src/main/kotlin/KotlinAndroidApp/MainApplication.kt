package KotlinAndroidApp

import android.app.Application
import com.project.kotlin_android_app.BuildConfig
import domain.Account
import domain.User
import domain.training.TrainingActions
import domain.training.TrainingHistory
import services.auth.AuthenticatorService
import services.auth.AuthenticatorServiceStub
import services.data.DataService
import services.data.DataServiceStub
import services.data.GrpcDataService
import services.training.GrpcTrainingService
import services.training.TrainingService
import services.training.TrainingServiceStub
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

    val authenticator: AuthenticatorService = AuthenticatorServiceStub()
    var dataRequester: DataService? = null
    private set

    var trainingService: TrainingService? = null
    private set

    var user = User.empty()
    val validator = Validator()
    lateinit var userSerializer: UserSerializer
    private set

    var currentTraining: TrainingActions? = null
    val trainingHistory: TrainingHistory = TrainingHistory(mutableListOf())

    override fun onCreate() {
        super.onCreate()
        xmlReader = XMLReader(this)
        userSerializer = UserSerializer(this)
    }

//    fun createServices(account: Account): Pair<DataService, TrainingService>{
//        dataRequester = GrpcDataService(
//            account.login,
//            account.password,
//            BuildConfig.serverAddress,
//            BuildConfig.serverPort.toInt()
//        )
//
//        trainingService = GrpcTrainingService(
//            account.login,
//            account.password,
//            BuildConfig.serverAddress,
//            BuildConfig.serverPort.toInt()
//        )
//
//        return Pair(dataRequester!!, trainingService!!)
//    }

    fun createServices(account: Account): Pair<DataService, TrainingService>{
        dataRequester = DataServiceStub()
        trainingService = TrainingServiceStub()
        return Pair(dataRequester!!, trainingService!!)
    }
}