package viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.raise.result
import auth.Authenticator
import domain.Account
import domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.UserSerializer
import utils.Validator

class RegistrationViewModel(
    private val authenticator: Authenticator,
    private val userSerializer: UserSerializer,
    private val user: User,
    private val validator: Validator
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

                //TODO remove field name from authenticator.register
                authenticator.register("", account.login, account.password).bind()
                Log.d("TSLA", "success register user")

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

                        is Authenticator.ServerConnectionException -> {
                            _errorMessage.value = "Нет подключения к серверу"
                        }

                        is Authenticator.InvalidCredentialsException -> {
                            _errorMessage.value = "Пользователь не найден"
                        }

                        is Authenticator.UserAlreadyExistsException -> {
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