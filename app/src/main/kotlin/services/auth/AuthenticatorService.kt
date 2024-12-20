package services.auth

import grpc.AuthServiceGrpc
import grpc.AuthServiceGrpc.AuthServiceBlockingStub
import grpc.DataServiceGrpc
import grpc.DataServiceGrpc.DataServiceBlockingStub
import io.grpc.ManagedChannelBuilder

/**
 * Интерфейс объекта для отправки запросов аутентификации на сервер
 */
interface AuthenticatorService {
    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String): Result<String>

    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Result<String>

    companion object {
        /**
         * Создает AuthServiceBlockingStub на канале по адресу url:port
         */
        fun createAuthServiceBlockingStub(url: String, port: Int): AuthServiceBlockingStub {
            return AuthServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress(url, port).usePlaintext().build()
            )
        }

        /**
         * Создает DataServiceBlockingStub на канале по адресу url:port
         */
        fun createDataServiceBlockingStub(url: String, port: Int): DataServiceBlockingStub {
            return DataServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress(url, port).usePlaintext().build()
            )
        }
    }
}
