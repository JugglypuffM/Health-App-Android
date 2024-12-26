package viewmodel

import android.util.Log
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
import utils.UserSerializer


class UserProfileViewModel(private val userSerializer: UserSerializer, private val user: User): ViewModel() {

    private val _onSuccess = MutableLiveData<Unit>()
    val onSuccess: LiveData<Unit> = _onSuccess;

    fun logout(){
        CoroutineScope(Dispatchers.IO).launch {
            val logOutResult = result {
                userSerializer.dropAccount()
                user.account = Account.empty()
                user.userInfo = UserInfo.empty()
                Log.d("TSLA", "Successfully logged out")
            }

            withContext(Dispatchers.Main) {
                logOutResult.onFailure { error ->
                    Log.d("TSLA", error.toString())
                }

                _onSuccess.value = Unit
            }
        }
    }
}