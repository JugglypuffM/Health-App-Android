package domain.training

import kotlinx.datetime.LocalDate
import kotlin.time.Duration

/**
 * Элемент тренировки
 * @param trainingInfo информация о тренировке
 * @param date дата тренировки
 * @param duration продолжительность тренировки
 */
data class TrainingEntity(
    val trainingInfo: TrainingInfo,
    val date: LocalDate,
    val duration: Duration
)