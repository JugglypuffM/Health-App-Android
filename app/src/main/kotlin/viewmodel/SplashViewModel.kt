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

class SplashViewModel(
    private val userSerializer: UserSerializer,
    private val authenticator: AuthenticatorService,
    private val user: User,
    private val validator: Validator,
    private val createDataRequest: (Account) -> Unit
) : ViewModel() {

    private val _onSuccess: MutableLiveData<Unit> = MutableLiveData<Unit>()
    val onSuccess: LiveData<Unit> = _onSuccess

    private val _onFailure: MutableLiveData<Unit> = MutableLiveData<Unit>()
    val onFailure: LiveData<Unit> = _onFailure

    private val _errorMessage: MutableLiveData<String> = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            val authResult = result {
                val account = userSerializer.loadAccount().bind()
                Log.d("TSLA", "Successfully loaded user $account")

                authenticator.login(account.login, account.password).bind()
                Log.d("TSLA", "Successfully login into account")

                createDataRequest(account)
                Log.d("TSLA", "Successfully create data request")

                user.account = account;
                account
            }

            withContext(Dispatchers.Main) {
                authResult.onSuccess {
                    _onSuccess.value = Unit
                }
                authResult.onFailure { error ->
                    when (error) {
                        is Exceptions.ServerConnectionException -> {
                            _errorMessage.value = "Произошла ошибка соединения с сервером"
                        }

                        is Exceptions.InvalidCredentialsException -> {
                            _errorMessage.value = "Данные для авторизации устарели"
                        }
                    }
                    Log.d("TSLA", "$error")
                    _onFailure.value = Unit
                }
            }
        }
    }
}