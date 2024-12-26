package services.training

import domain.training.Training
import kotlinx.datetime.LocalDate

interface TrainingService {
    /**
     * Функция для записи тренировки в бд
     * @param training тренировка, которую нужно сохранить
     */
    suspend fun saveTraining(training: Training): Result<Unit>

    /**
     * Функция для получения списка тренировок на конкретную дату
     * @param date дата на которую получаем
     */
    suspend fun getTrainings(date: LocalDate): Result<List<Training>>
}