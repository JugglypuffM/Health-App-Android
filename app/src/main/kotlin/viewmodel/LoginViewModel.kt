package viewmodel

import android.content.Context
import auth.Authenticator
import data.DataRequester
import domain.BasicUserData
import domain.User
import utils.Validator
import utils.UserSerializer

class LoginViewModel(private val storage: UserSerializer, private val authenticator: Authenticator, private val dataRequester: DataRequester, private val validator: Validator) : ViewModel{
    override suspend fun login(login: String, password: String): Result<String> {
        return authenticator.login(login, password)
    }

    override suspend fun register(name: String, login: String, password: String): Result<String> {
        return authenticator.register(name, login, password)
    }

    override suspend fun validate(login: String, password: String): Result<User> {
        return validator.check(login, password)
    }

    override suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Result<User> {
        return validator.check(name, login, password, confirmPassword)
    }

    override suspend fun loadUser(): Result<User> {
        return storage.loadUser()
    }

    override fun dropUser(): Result<String>{
        return storage.dropUser()
    }

    override fun setContext(applicationContext: Context) {
        return storage.setContext(applicationContext)
    }

    override fun saveUser(value: User): Result<String> {
        return storage.saveUser(value)
    }

    override suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> {
        return dataRequester.getBasicUserData(login, password)
    }
}