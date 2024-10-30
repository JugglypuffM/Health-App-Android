package viewmodel

import android.content.Context
import android.util.Log
import auth.Authenticator
import domain.User
import domain.Validate
import domain.flatMap
import utils.LocalStorage

class LoginViewModel(private val storage: LocalStorage, private val authenticator: Authenticator) : ViewModel{
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

    /**
     * Валидация имени
     * @param name имя пользователя
     */
    private fun validateName(name: String): Result<String> {
        return if (name.isBlank())
            Result.failure(Validate.InvalidNameException("User name is empty"))
        else Result.success(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    private fun validateLogin(login: String): Result<String> {
        return if (login.isBlank())
            Result.failure(Validate.InvalidLoginException("Login is empty"))
        else Result.success(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    private fun validatePassword(password: String): Result<String> {
        return if (password.length < 6)
            Result.failure(Validate.InvalidPasswordException("The password must be at least 6 characters long"))
        else Result.success(password)
    }

    /**
     * Валидация совпадения пароля
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     */
    private fun validateConfirmPassword(password: String, confirmPassword: String): Result<String> {
        return if (password != confirmPassword)
            Result.failure(Validate.NotEqualPasswordException("Passwords do not match"))
        else Result.success(password)
    }

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    override suspend fun validate(login: String, password: String): Result<User> {
        return validateLogin(login).flatMap { _ ->
            validatePassword(password).map {_ ->
                User(null, login, password)
            }
        }
    }

    /**
     * Проверка валидности имени, логина, пароля и подтверждения пароля
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     * @param confirmPassword Подтверждение пароля пользователя
     */
    override suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Result<User> {
        return validateName(name).flatMap {_ ->
            validateLogin(login).flatMap { _ ->
                validateConfirmPassword(password, confirmPassword).flatMap { _ ->
                    validatePassword(password).map {_ ->
                        User(name, login, password)
                    }
                }
            }
        }
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