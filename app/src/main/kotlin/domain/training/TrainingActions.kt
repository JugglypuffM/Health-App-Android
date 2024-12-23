package domain.training

import com.project.kotlin_android_app.R
import java.io.Serializable

/**
 * Список действий для конкретной тренировки
 * @param actions список действий
 */
enum class TrainingActions(var actions: List<TrainingAction>): Serializable {
    Yoga (
        listOf(
            TrainingAction("Подготовьтесь", 12000, R.drawable.ic_training_waiting),
            TrainingAction("Тадасана", 62000, R.drawable.ic_yoga_tadasana),
            TrainingAction("Перерыв", 10000, R.drawable.ic_training_waiting),
            TrainingAction("Врикшасана", 62000, R.drawable.ic_yoga_vrksasana),
            TrainingAction("Перерыв", 10000, R.drawable.ic_training_waiting),
            TrainingAction("Вирабхадрасана", 62000, R.drawable.ic_yoga_virabhadrasana),
            TrainingAction("Перерыв", 10000, R.drawable.ic_training_waiting),
            TrainingAction("Бхуджангасана", 62000, R.drawable.ic_yoga_bhujangasana),
        )
    );
}