package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.raise.result
import domain.UserInfo
import domain.exceptions.Exceptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import services.data.DataService
import utils.CustomLogger
import utils.UserSerializer

class UserProfileViewModel(
    private val dataService: DataService?,
    private val userSerializer: UserSerializer,
    info: UserInfo,
    private val logger: CustomLogger
) : ViewModel() {
    private val _userInfo = MutableLiveData(info)
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _onFinish = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val dataResult = dataService?.getUserData()
                ?: Result.failure(Exceptions.UnexpectedError("DataRequester was not initialized"))

            withContext(Dispatchers.Main) {
                dataResult.onFailure {
                    _errorMessage.value = when (it) {
                        is Exceptions.InvalidCredentialsException -> "Данные для авторизации устарели"
                        is Exceptions.ServerConnectionException -> "Произошла ошибка соединения с сервером"
                        else -> "Непредвиденная ошибка"
                    }

                    logger.logDebug(it.toString())
                }

                dataResult.onSuccess { _userInfo.value = it }
            }
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            val logOutResult = result {
                userSerializer.dropAccount()
            }

            withContext(Dispatchers.Main) {
                logOutResult.onFailure { error ->
                    logger.logDebug(error.toString())
                }

                _onFinish.value = Unit
            }
        }
    }
}