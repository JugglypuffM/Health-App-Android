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

class RegistrationViewModel(
    private val authenticator: AuthenticatorService,
    private val userSerializer: UserSerializer,
    private val user: User,
    private val validator: Validator,
    private val createServices: (Account) -> Unit
) : ViewModel() {

    private val _onSuccess = MutableLiveData<Unit>()
    val onSuccess: LiveData<Unit> = _onSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun check(rawLogin: String, rawPassword: String, confirmPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = result {
                val login = validator.validateLogin(rawLogin).bind()
                val password = validator.validatePassword(rawPassword).bind()
                validator.validateConfirmPassword(password, confirmPassword).bind()
                val account = Account(login, password)
                Log.d("TSLA", "success validate user account")

                authenticator.register("", account.login, account.password).bind()
                Log.d("TSLA", "success register user")

                createServices(account)
                Log.d("TSLA", "success create data request")

                userSerializer.saveAccount(account).bind()
                user.account = account
                Log.d("TSLA", "success save account")

                account
            }

            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _onSuccess.value = Unit
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

                        else -> {
                            _errorMessage.value = "Непредвиденная ошибка"
                        }
                    }

                    Log.e("TSLA", "throw user error: $error")
                }
            }
        }
    }
}