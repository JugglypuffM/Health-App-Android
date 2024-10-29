package viewmodel

import android.content.Context
import android.util.Log
import domain.Either
import domain.User
import domain.Validate

interface ViewModel {
    /**
     * Удалённый логин пользователя
     * @param name Имя пользователя
     * @param password Пароль пользователя
     */
    suspend fun login(name: String, password: String): Either<Throwable, User>

    /**
     * Удалённая регистрация пользователя
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    suspend fun register(name: String, login: String, password: String): Either<Throwable, User>

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    suspend fun validate(login: String, password: String): Either<Throwable, User>

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Either<Throwable, User>

    /**
     * Загрузка пользователя из хранилища
     */
    suspend fun loadUser(): Either<Throwable, User>

    /**
     * Удалить пользователя из хранилища
     */
    fun dropUser()

    /**
     * Установка контекста приложения для работы с хранилищем
     */
    fun setContext(applicationContext: Context)

    /**
     * Сохранение пользователя в хранилище
     */
    fun saveUser(value: User)
}