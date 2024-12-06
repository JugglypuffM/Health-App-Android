package domain.training

import java.io.Serializable

/**
 * Список действий для конкретной тренировки
 * @param actions список действий
 */
enum class TrainingActions(var actions: List<TrainingAction>): Serializable {
    Yoga (
        listOf(
            TrainingAction("Подготовьтесь", 12000), // Подготовка
            TrainingAction("Перерыв", 10000),        // Временной интервал
            TrainingAction("Поза 1: Гора", 62000),   // Первая поза
            TrainingAction("Перерыв", 10000),        // Временной интервал
            TrainingAction("Поза 2: Дерево", 62000), // Вторая поза
            TrainingAction("Перерыв", 10000),        // Временной интервал
            TrainingAction("Поза 3: Воин", 62000),   // Третья поза
            TrainingAction("Перерыв", 10000),        // Временной интервал
            TrainingAction("Поза 4: Кобра", 62000),  // Четвертая поза
            TrainingAction("Перерыв", 10000),        // Временной интервал
            TrainingAction("Завершение занятия", 12000) // Завершение
        )
    );
}