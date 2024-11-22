package domain.training

import kotlinx.datetime.LocalDate
import kotlin.time.Duration

/**
 * Элемент тренировки
 * @param trainingType информация о тренировке
 * @param date дата тренировки
 * @param duration продолжительность тренировки
 */

data class TrainingEntity(
    val trainingType: TrainingType,
    val date: LocalDate,
    val duration: Duration
)