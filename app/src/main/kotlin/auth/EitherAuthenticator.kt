package auth

import domain.Either
import domain.User

/**
 * Интерфейс объекта для отправки запросов аутентификации на сервер, написанный на Either
 */
interface EitherAuthenticator {
    class ServerConnectionException(message: String): Exception(message)
    class InvalidCredentialsException(message: String) : Exception(message)
    class UserAlreadyExistsException(message: String) : Exception(message)

    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String): Either<Throwable, User>

    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Either<Throwable, User>
}