package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.raise.result
import domain.Account
import domain.User
import domain.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.CustomLogger
import utils.UserSerializer

class UserProfileViewModel(
    private val userSerializer: UserSerializer,
    private val user: User,
    private val logger: CustomLogger
): ViewModel() {

    private val _onFinish = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish;

    fun logout(){
        CoroutineScope(Dispatchers.IO).launch {
            val logOutResult = result {
                userSerializer.dropAccount()
                user.account = Account.empty()
                user.userInfo = UserInfo.empty()
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