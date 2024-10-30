package viewmodel

import android.content.Context
import domain.User

interface ViewModel {
    /**
     * Удалённый логин пользователя
     * @param name Имя пользователя
     * @param password Пароль пользователя
     */
    suspend fun login(name: String, password: String): Result<String>

    /**
     * Удалённая регистрация пользователя
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    suspend fun register(name: String, login: String, password: String): Result<String>

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    suspend fun validate(login: String, password: String): Result<User>

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Result<User>

    /**
     * Загрузка пользователя из хранилища
     */
    suspend fun loadUser(): Result<User>

    /**
     * Удалить пользователя из хранилища
     */
    fun dropUser(): Result<String>

    /**
     * Установка контекста приложения для работы с хранилищем
     */
    fun setContext(applicationContext: Context)

    /**
     * Сохранение пользователя в хранилище
     */
    fun saveUser(value: User): Result<String>
}