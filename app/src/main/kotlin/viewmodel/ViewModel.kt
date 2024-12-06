package viewmodel

import services.auth.AuthenticatorService
import services.data.DataService
import domain.BasicUserData
import domain.User
import utils.Validator
import utils.UserSerializer

class ViewModel(private val userSerializer: UserSerializer, private val authenticatorService: AuthenticatorService, private val dataService: DataService, private val validator: Validator){
    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Result<String> {
        return authenticatorService.login(login, password)
    }

    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String): Result<String> {
        return authenticatorService.register(name, login, password)
    }

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    fun validate(login: String, password: String): Result<User> {
        return validator.check(login, password)
    }

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    fun validate(name: String, login: String, password: String, confirmPassword: String): Result<User> {
        return validator.check(name, login, password, confirmPassword)
    }

    /**
     * Загрузить пользователя
     */
    fun loadUser(): Result<User> {
        return userSerializer.loadUser()
    }

    /**
     * Удалить пользователя
     */
    fun dropUser(): Result<String> {
        return userSerializer.dropUser()
    }

    /**
     * Сохранить пользователя
     */
    fun saveUser(value: User): Result<String> {
        return userSerializer.saveUser(value)
    }

    /**
     * Функция для запроса BasicUserData
     */
    suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData> {
        return dataService.getBasicUserData(login, password)
    }
}