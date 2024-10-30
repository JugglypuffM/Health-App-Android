package viewmodel

import android.content.Context
import auth.Authenticator
import domain.User
import utils.Validator
import utils.LocalStorage

class LoginViewModel(private val storage: LocalStorage, private val authenticator: Authenticator, private val validator: Validator) : ViewModel{
    /**
     * Удалённый логин пользователя
     * @param name Имя пользователя
     * @param password Пароль пользователя
     */
    override suspend fun login(name: String, password: String): Result<String> {
        return authenticator.login(name, password)
    }

    /**
     * Удалённая регистрация пользователя
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    override suspend fun register(name: String, login: String, password: String): Result<String> {
        return authenticator.register(name, login, password)
    }

    override suspend fun validate(login: String, password: String): Result<User> {
        return validator.check(login, password)
    }

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    override suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Result<User> {
        return validator.check(name, login, password, confirmPassword)
    }

    /**
     * Загрузка пользователя из хранилища
     */
    override suspend fun loadUser(): Result<User> {
        return storage.loadUser()
    }

    /**
     * Удалить пользователя из хранилища
     */
    override fun dropUser(): Result<String>{
        return storage.dropUser()
    }

    /**
     * Установка контекста приложения для работы с хранилищем
     */
    override fun setContext(applicationContext: Context) {
        return storage.setContext(applicationContext)
    }

    /**
     * Сохранение пользователя в хранилище
     */
    override fun saveUser(value: User): Result<String> {
        return storage.saveUser(value)
    }
}