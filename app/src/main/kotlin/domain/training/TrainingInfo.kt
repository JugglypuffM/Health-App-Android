package domain.training

/**
 * Информация о тренировке
 * Специфична для каждого типа тренировки
 */
interface TrainingInfo {
    fun getTitle(): String
    fun getDescription(): String
}