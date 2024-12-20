package services.training

import domain.training.Training
import kotlinx.datetime.LocalDate

interface TrainingService {
    /**
     * Функция для записи тренировки в бд
     * @param login логин пользователя который провел тренировку
     * @param password пароль данного пользователя для авторизации
     * @param training тренировка, которую нужно сохранить
     */
    suspend fun saveTraining(login: String, password: String, training: Training): Result<Unit>

    /**
     * Функция для получения списка тренировок на конкретную дату
     * @param date дата на которую получаем
     */
    suspend fun getTrainings(
        login: String,
        password: String,
        date: LocalDate
    ): Result<List<Training>>
}