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

    /**
     * Функция для обновления данных о пользователе
     * Данные о дистанции обновляются на сервере, поэтому не важно что хранится в этом поле здесь
     * @param login логин пользователя
     * @param password пароль пользователя
     * @param info класс [UserInfo] с информацией о пользователе
     */
    suspend fun updateUserData(login: String, password: String, info: UserInfo): Result<Unit>
}