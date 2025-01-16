package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.raise.result
import domain.Account
import domain.User
import domain.exceptions.Exceptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import services.auth.AuthenticatorService
import services.data.DataService
import services.training.TrainingService
import utils.CustomLogger
import utils.UserSerializer

class SplashViewModel(
    private val userSerializer: UserSerializer,
    private val authenticator: AuthenticatorService,
    private val user: User,
    private val createServices: (Account) -> Pair<DataService, TrainingService>,
    private val logger: CustomLogger
) : ViewModel() {

    private val _onFinish: MutableLiveData<Unit> = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish

    private val _onFailure: MutableLiveData<Unit> = MutableLiveData<Unit>()
    val onFailure: LiveData<Unit> = _onFailure

    private val _errorMessage: MutableLiveData<String> = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            val authResult = result {
                val account = userSerializer.loadAccount().bind()

                authenticator.login(account.login, account.password).bind()

                val (dataService, _) = createServices(account)

                val userInfo = dataService.getUserData().bind()

                user.account = account
                user.userInfo = userInfo
                account
            }

            withContext(Dispatchers.Main) {
                authResult.onSuccess {
                    _onFinish.value = Unit
                }
                authResult.onFailure { error ->
                    when (error) {
                        is Exceptions.ServerConnectionException -> {
                            _errorMessage.value = "Произошла ошибка соединения с сервером"
                        }

                        is Exceptions.InvalidCredentialsException -> {
                            userSerializer.dropAccount()
                            _errorMessage.value = "Данные для авторизации устарели"
                        }

                        else -> {
                            _onFinish.value = Unit
                        }
                    }

                    logger.logDebug(error.toString())
                    _onFailure.value = Unit
                }
            }
        }
    }
}