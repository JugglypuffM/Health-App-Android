package services.data

import domain.UserInfo

/**
 * Интерфейс объекта для запроса данных пользователя с сервера
 */
interface DataService {

    /**
     * Функция запроса основной информации о пользователе
     */
    suspend fun getUserData(): Result<UserInfo>

    /**
     * Функция для обновления данных о пользователе
     * Данные о дистанции обновляются на сервере, поэтому не важно что хранится в этом поле здесь
     * @param info класс [UserInfo] с информацией о пользователе
     */
    suspend fun updateUserData(info: UserInfo): Result<Unit>
}