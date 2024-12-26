package viewmodel

import services.auth.AuthenticatorService
import services.data.DataService
import domain.Account
import domain.UserInfo
import utils.Validator
import utils.UserSerializer

class ViewModel(private val userSerializer: UserSerializer, private val authenticatorService: AuthenticatorService, private val dataService: DataService, private val validator: Validator){
    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Result<Unit> {
        return authenticatorService.login(login, password)
    }

    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String): Result<Unit> {
        return authenticatorService.register(name, login, password)
    }

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    fun validate(login: String, password: String): Result<Account> {
        return validator.check(login, password)
    }

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    fun validate(name: String, login: String, password: String, confirmPassword: String): Result<Account> {
        return validator.check(name, login, password, confirmPassword)
    }

    /**
     * Загрузить пользователя
     */
    suspend fun loadAccount(): Result<Account>{
        return userSerializer.loadAccount()
    }

    /**
     * Удалить пользователя
     */
    fun dropAccount(): Result<String> {
        return userSerializer.dropAccount()
    }

    /**
     * Сохранить пользователя
     */
    fun saveAccount(value: Account): Result<String>{
        return userSerializer.saveAccount(value)
    }

    /**
     * Функция для запроса BasicUserData
     */
    suspend fun getUserData(): Result<UserInfo>{
        return dataService.getUserData()
    }
}