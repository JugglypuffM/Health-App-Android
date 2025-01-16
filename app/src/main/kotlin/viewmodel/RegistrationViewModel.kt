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
import utils.CustomLogger
import utils.UserSerializer
import utils.Validator

class RegistrationViewModel(
    private val authenticator: AuthenticatorService,
    private val userSerializer: UserSerializer,
    private val user: User,
    private val validator: Validator,
    private val createServices: (Account) -> Unit,
    private val logger: CustomLogger
) : ViewModel() {

    private val _onFinish = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun check(rawLogin: String, rawPassword: String, confirmPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = result {
                val login = validator.validateLogin(rawLogin).bind()
                val password = validator.validatePassword(rawPassword).bind()
                validator.validateConfirmPassword(password, confirmPassword).bind()
                val account = Account(login, password)

                //TODO переписать register без передачи имени
                authenticator.register("", account.login, account.password).bind()

                createServices(account)

                userSerializer.saveAccount(account).bind()
                user.account = account

                account
            }

            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _onFinish.value = Unit
                }
                result.onFailure { error ->
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

                        is Validator.NotEqualPasswordException -> {
                            _errorMessage.value = "Пароли не совпадают"
                        }

                        is Exceptions.ServerConnectionException -> {
                            _errorMessage.value = "Нет подключения к серверу"
                        }

                        is Exceptions.InvalidCredentialsException -> {
                            _errorMessage.value = "Пользователь не найден"
                        }

                        is Exceptions.UserAlreadyExistsException -> {
                            _errorMessage.value = "Пользователь с таким логином уже существует"
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