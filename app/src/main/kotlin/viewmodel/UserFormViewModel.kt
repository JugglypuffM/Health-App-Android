import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.raise.result
import domain.User
import domain.UserInfo
import domain.exceptions.Exceptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import services.data.DataService
import utils.CustomLogger
import utils.Validator

class UserFormViewModel(
    private val user: User,
    private val validator: Validator,
    private val dataService: DataService,
    private val logger: CustomLogger
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _onFinish = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish

    fun check(rawName: String, rawAge: String, rawWeight: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val checkResult = result {
                val name = validator.validateName(rawName).bind()
                val age = validator.validateAge(rawAge.toIntOrNull()).bind()
                val weight = validator.validateWeight(rawWeight.toIntOrNull()).bind()
                val userInfo = UserInfo(name, age, weight, user.userInfo.distance)

                dataService.updateUserData(userInfo).bind()

                userInfo
            }

            withContext(Dispatchers.Main) {
                checkResult.onSuccess { userInfo ->
                    user.userInfo = userInfo
                    _onFinish.value = Unit
                }

                checkResult.onFailure { error ->
                    _errorMessage.value = when (error) {
                        is Validator.InvalidNameException -> "Неверное имя пользователя"
                        is Validator.InvalidAgeException -> "Неверный возраст"
                        is Validator.InvalidWeightException -> "Неверный вес"
                        is Exceptions.InvalidCredentialsException -> "Данные для авторизации устарели"
                        is Exceptions.ServerConnectionException -> "Произошла ошибка соединения с сервером"
                        else -> "Непредвиденная ошибка"
                    }

                    logger.logDebug(error.toString())
                }
            }
        }
    }


}
