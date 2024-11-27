package viewmodel

import android.content.Context
import auth.Authenticator
import data.DataRequester
import domain.Account
import domain.UserInfo
import utils.UserSerializer
import utils.Validator

class LoginViewModel(
    private val storage: UserSerializer,
    private val authenticator: Authenticator,
    private val dataRequester: DataRequester,
    private val validator: Validator
) : ViewModel {
    override suspend fun login(login: String, password: String): Result<String> {
        return authenticator.login(login, password)
    }

    override suspend fun register(name: String, login: String, password: String): Result<String> {
        return authenticator.register(name, login, password)
    }

    override suspend fun validate(login: String, password: String): Result<Account> {
        return validator.check(login, password)
    }

    override suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Result<Account> {
        return validator.check(name, login, password, confirmPassword)
    }

    override suspend fun loadAccount(): Result<Account> {
        return storage.loadAccount()
    }

    override fun dropUser(): Result<String>{
        return storage.dropAccount()
    }

    override fun setContext(applicationContext: Context) {
        return storage.setContext(applicationContext)
    }

    override fun saveAccount(value: Account): Result<String> {
        return storage.saveAccount(value)
    }

    override suspend fun getBasicUserData(login: String, password: String): Result<UserInfo> {
        return dataRequester.getUserData(login, password)
    }
}