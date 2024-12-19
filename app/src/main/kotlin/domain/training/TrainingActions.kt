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
    ),

    StrengthTraining(
        listOf(
            TrainingAction("Подготовьтесь", 12000, R.drawable.ic_training_waiting),
            TrainingAction("Приседания с гантелями", 40000, R.drawable.ic_strength_squat),
            TrainingAction("Перерыв", 20000, R.drawable.ic_training_waiting),
            TrainingAction("Жим штанги лежа", 40000, R.drawable.ic_strength_bench_press),
            TrainingAction("Перерыв", 20000, R.drawable.ic_training_waiting),
            TrainingAction("Подтягивания", 40000, R.drawable.ic_strength_pull_up),
            TrainingAction("Перерыв", 20000, R.drawable.ic_training_waiting),
            TrainingAction("Тяга гантели к поясу", 40000, R.drawable.ic_strength_dumbbell_row),
        )
    )

    Cardio(
        listOf(
            TrainingAction("Подготовьтесь", 12000, R.drawable.ic_training_waiting),
            TrainingAction("Бег на месте", 60000, R.drawable.ic_cardio_running),
            TrainingAction("Перерыв", 15000, R.drawable.ic_training_waiting),
            TrainingAction("Прыжки со скакалкой", 60000, R.drawable.ic_cardio_jump_rope),
            TrainingAction("Перерыв", 15000, R.drawable.ic_training_waiting),
            TrainingAction("Берпи", 60000, R.drawable.ic_cardio_burpee),
            TrainingAction("Перерыв", 15000, R.drawable.ic_training_waiting),
            TrainingAction("Высокие колени", 60000, R.drawable.ic_cardio_high_knees),
        )
    ),

    Tabata (
        listOf(
            TrainingAction("Подготовьтесь", 12000, R.drawable.ic_training_waiting),
            TrainingAction("Спринт", 20000, R.drawable.ic_tabata_sprint),
            TrainingAction("Перерыв", 10000, R.drawable.ic_training_waiting),
            TrainingAction("Прыжки на месте", 20000, R.drawable.ic_tabata_jump),
            TrainingAction("Перерыв", 10000, R.drawable.ic_training_waiting),
            TrainingAction("Отжимания", 20000, R.drawable.ic_tabata_push_ups),
            TrainingAction("Перерыв", 10000, R.drawable.ic_training_waiting),
            TrainingAction("Приседания", 20000, R.drawable.ic_tabata_squats),
        )
    ),

    FunctionalTraining(
        listOf(
            TrainingAction("Подготовьтесь", 12000, R.drawable.ic_training_waiting),
            TrainingAction("Кубковые приседания с гирей", 40000, R.drawable.ic_functional_goblet_squat),
            TrainingAction("Перерыв", 15000, R.drawable.ic_training_waiting),
            TrainingAction("Жим гирь над головой", 40000, R.drawable.ic_functional_overhead_press),
            TrainingAction("Перерыв", 15000, R.drawable.ic_training_waiting),
            TrainingAction("Тяга к груди с эластичной лентой", 40000, R.drawable.ic_functional_band_pull),
            TrainingAction("Перерыв", 15000, R.drawable.ic_training_waiting),
            TrainingAction("Планка с подъемом рук", 40000, R.drawable.ic_functional_plank),
        )
    )
}