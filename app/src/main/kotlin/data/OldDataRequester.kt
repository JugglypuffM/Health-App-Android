package data

import domain.BasicUserData

/**
 * Интерфейс объекта для запроса данных пользователя с сервера
 */
@Deprecated("Use new DataRequester")
interface OldDataRequester {

    /**
     * Функция запроса основной информации о пользователе
     * @param login логин пользователя
     * @param password пароль пользователя
     */
    suspend fun getBasicUserData(login: String, password: String): Result<BasicUserData>
}