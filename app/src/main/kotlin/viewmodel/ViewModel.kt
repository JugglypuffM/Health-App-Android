package viewmodel

import android.content.Context
import domain.Account
import domain.UserInfo

interface ViewModel {
    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Result<String>

    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String): Result<String>

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    suspend fun validate(login: String, password: String): Result<Account>

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Result<Account>

    /**
     * Загрузить пользователя
     */
    suspend fun loadAccount(): Result<Account>

    /**
     * Удалить пользователя
     */
    fun dropUser(): Result<String>

    /**
     * Установить контекст
     */
    fun setContext(applicationContext: Context)

    /**
     * Сохранить пользователя
     */
    fun saveAccount(value: Account): Result<String>

    /**
     * Функция для запроса BasicUserData
     */
    suspend fun getBasicUserData(login: String, password: String): Result<UserInfo>
}