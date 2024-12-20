package services.data

import domain.UserInfo

/**
 * Интерфейс объекта для запроса данных пользователя с сервера
 */
interface DataService {

    /**
     * Функция запроса основной информации о пользователе
     * @param login логин пользователя
     * @param password пароль пользователя
     */
    suspend fun getUserData(login: String, password: String): Result<UserInfo>
}