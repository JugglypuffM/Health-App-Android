package viewmodel

import android.content.Context
import android.util.Log
import auth.Authenticator
import domain.Either
import domain.User
import domain.Validate
import utils.LocalStorage

class LoginViewModel(private val storage: LocalStorage, private val authenticator: Authenticator) : ViewModel{
    /**
     * Удалённый логин пользователя
     * @param name Имя пользователя
     * @param password Пароль пользователя
     */
    override suspend fun login(name: String, password: String): Either<Throwable, User> {
        return authenticator.login(name, password)
    }

    /**
     * Удалённая регистрация пользователя
     * @param name Имя пользователя
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    override suspend fun register(name: String, login: String, password: String): Either<Throwable, User> {
        return authenticator.register(name, login, password).map {user ->
            storage.saveUser(user)
            user
        }
    }

    /**
     * Валидация имени
     * @param name имя пользователя
     */
    private fun validateName(name: String): Either<Throwable, String> {
        return if (name.isBlank())
            Either.Left(Validate.InvalidNameException("User name is empty"))
        else Either.Right(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    private fun validateLogin(login: String): Either<Throwable, String> {
        return if (login.isBlank())
            Either.Left(Validate.InvalidLoginException("Login is empty"))
        else Either.Right(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    private fun validatePassword(password: String): Either<Throwable, String> {
        return if (password.length < 6)
            Either.Left(Validate.InvalidPasswordException("The password must be at least 6 characters long"))
        else Either.Right(password)
    }

    /**
     * Валидация совпадения пароля
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     */
    private fun validateConfirmPassword(password: String, confirmPassword: String): Either<Throwable, String> {
        return if (password != confirmPassword)
            Either.Left(Validate.NotEqualPasswordException("Passwords do not match"))
        else Either.Right(password)
    }

    /**
     * Проверка валидности логина и пароля
     * @param login Логин пользователя
     * @param password Пароль пользователя
     */
    override suspend fun validate(login: String, password: String): Either<Throwable, User> {
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
    override suspend fun validate(name: String, login: String, password: String, confirmPassword: String): Either<Throwable, User> {
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
    override suspend fun loadUser(): Either<Throwable, User> {
        Log.d("MYDB", "loaded either: ${storage.loadUser()}")
        return storage.loadUser()
    }

    /**
     * Удалить пользователя из хранилища
     */
    override fun dropUser(){
        storage.dropUser()
    }

    /**
     * Установка контекста приложения для работы с хранилищем
     */
    override fun setContext(applicationContext: Context) {
        storage.setContext(applicationContext)
    }

    /**
     * Сохранение пользователя в хранилище
     */
    override fun saveUser(value: User) {
        storage.saveUser(value)
    }
}