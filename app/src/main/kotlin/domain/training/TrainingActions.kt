package domain.training

import java.io.Serializable

/**
 * Список действий для конкретной тренировки
 * @param actions список действий
 */
enum class TrainingActions(var actions: List<TrainingAction>): Serializable {
    Yoga (
        listOf(
            TrainingAction("Подготовьтесь", 12000),
            TrainingAction("Поза 1: Гора", 62000),
            TrainingAction("Перерыв", 10000),
            TrainingAction("Поза 2: Дерево", 62000),
            TrainingAction("Перерыв", 10000),
            TrainingAction("Поза 3: Воин", 62000),
            TrainingAction("Перерыв", 10000),
            TrainingAction("Поза 4: Кобра", 62000),
            TrainingAction("Перерыв", 10000),
            TrainingAction("Завершение занятия", 12000)
        )
    );
}