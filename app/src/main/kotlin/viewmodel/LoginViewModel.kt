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
import utils.Validator

class LoginViewModel(
    private val authenticator: AuthenticatorService,
    private val userSerializer: UserSerializer,
    private val user: User,
    private val validator: Validator,
    private val createServices: (Account) -> Pair<DataService, TrainingService>,
    private val logger: CustomLogger
) : ViewModel() {

    private val _onFinish = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun check(rawLogin: String, rawPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val authResult = result {
                val login = validator.validateLogin(rawLogin).bind()
                val password = validator.validatePassword(rawPassword).bind()
                val account = Account(login, password)

                authenticator.login(account.login, account.password).bind()

                val (dataService, _) = createServices(account)
                val userInfo = dataService.getUserData().bind()
                userSerializer.saveAccount(account).bind()
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
                        is Validator.InvalidNameException -> {
                            _errorMessage.value = "Неверное имя пользователя"
                        }

                        is Validator.InvalidLoginException -> {
                            _errorMessage.value = "Неверный логин"
                        }

                        is Validator.InvalidPasswordException -> {
                            _errorMessage.value = "Неверный пароль"
                        }

                        is Exceptions.ServerConnectionException -> {
                            _errorMessage.value = "Нет подключения к серверу"
                        }

                        is Exceptions.InvalidCredentialsException -> {
                            _errorMessage.value = "Пользователь не найден"
                        }

                        is Exceptions.UnexpectedError -> {
                            _errorMessage.value = "Проблема соединения с сервером"
                        }

                        else -> {
                            _errorMessage.value = "Непредвиденная ошибка"
                        }
                    }

                    logger.logDebug(error.toString())
                }
            }
        }
    }
}