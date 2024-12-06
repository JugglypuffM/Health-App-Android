package domain.training

/**
 * Данные о действии тренировки
 * @param title сообщение
 * @param timeUntilMillis время на выполнение
 */
data class TrainingAction(val title: String, val timeUntilMillis: Long)