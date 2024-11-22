package domain.training

import kotlinx.datetime.LocalDate
import kotlin.time.Duration

/**
 * Информация о тренировке
 * Специфична для каждого типа тренировки
 */
sealed class Training(val date: LocalDate, val duration: Duration) {
    /**
     * Информация о йоге
     */
    class Yoga(date: LocalDate, duration: Duration) : Training(date, duration)

    /**
     * Информация о беге
     */
    class Jogging(date: LocalDate, duration: Duration, val distance: Double) : Training(date, duration)
}