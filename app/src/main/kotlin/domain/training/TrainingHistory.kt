package domain.training

/**
 * История тренировок
 */
class TrainingHistory(
    value: List<Training>
) {
    var value = value
    private set

    operator fun plus(training: Training): TrainingHistory {
        val newList = value + training
        return TrainingHistory(newList)
    }
}