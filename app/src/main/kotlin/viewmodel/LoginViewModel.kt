package viewmodel

import android.util.Log
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
import utils.UserSerializer
import utils.Validator

class LoginViewModel(
    private val authenticator: AuthenticatorService,
    private val userSerializer: UserSerializer,
    private val user: User,
    private val validator: Validator,
    private val createDataService: (Account) -> Unit
) : ViewModel() {

    private val _onSuccess = MutableLiveData<Unit>()
    val onSuccess: LiveData<Unit> = _onSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun check(rawLogin: String, rawPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val authResult = result {
                val login = validator.validateLogin(rawLogin).bind()
                val password = validator.validatePassword(rawPassword).bind()
                val account = Account(login, password)
                Log.d("TSLA", "success validate account")

                authenticator.login(account.login, account.password).bind()
                Log.d("TSLA", "login in service success")

                createDataService(account)
                Log.d("TSLA", "success create data requester")

                userSerializer.saveAccount(account).bind()
                user.account = account
                Log.d("TSLA", "success save account")

                account
            }

            withContext(Dispatchers.Main) {
                authResult.onSuccess {
                    _onSuccess.value = Unit
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

                        else -> {
                            _errorMessage.value = "Непредвиденная ошибка"
                        }
                    }

                    Log.d("TSLA", error.toString())
                }
            }
        }
    }
}