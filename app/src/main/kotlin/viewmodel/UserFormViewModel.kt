import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.raise.result
import domain.User
import domain.UserInfo
import utils.Validator

class UserFormViewModel(private val user: User, private val validator: Validator) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _navigateToNewActivity = MutableLiveData<Unit>()
    val onSuccess: LiveData<Unit> = _navigateToNewActivity

    fun check(rawName: String, rawAge: String, rawWeight: String) {
        val checkResult = result {
            val name = validator.validateName(rawName).bind()
            val age = validator.validateAge(rawAge.toIntOrNull()).bind()
            val weight = validator.validateWeight(rawWeight.toIntOrNull()).bind()
            UserInfo(name, age, weight, 0)
        }

        checkResult.onSuccess { userInfo ->
            user.userInfo = userInfo
            _navigateToNewActivity.value = Unit
        }

        checkResult.onFailure { error ->
            _errorMessage.value = when (error) {
                is Validator.InvalidNameException -> "Неверное имя пользователя"
                is Validator.InvalidAgeException -> "Неверный возраст"
                is Validator.InvalidWeightException -> "Неверный вес"
                else -> {
                    Log.d("MLG", error.toString())
                    "Непредвиденная ошибка"
                }
            }
        }
    }


}
