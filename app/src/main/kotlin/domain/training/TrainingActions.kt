package domain.training

import com.project.kotlin_android_app.R
import java.io.Serializable

/**
 * Список действий для конкретной тренировки
 * @param actions список действий
 */
enum class TrainingActions(val value: List<TrainingAction>){
    Yoga(
        listOf(
            TrainingAction("Подготовьтесь", 10000, R.drawable.waiting),
            TrainingAction("Тадасана", 60000, R.drawable.yoga_tadasana),
            TrainingAction("Врикшасана", 60000, R.drawable.yoga_super_tadasana),
            TrainingAction("Адхо Мукха Шванасана", 90000, R.drawable.yoga_dog_face_down),
            TrainingAction("Чатуранга Дандасана", 30000, R.drawable.yoga_dog_face_up),
            TrainingAction("Урдхва Мукха Шванасана", 60000, R.drawable.yoga_half_dog_face_down),
            TrainingAction("Вирабхадрасана I", 90000, R.drawable.yoga_varior_pose)
        )
    ),

    FullBodyStrength(
        listOf(
            TrainingAction("Приготовьтесь", 10000, R.drawable.waiting),
            TrainingAction("Наклоны в сторону", 30000, R.drawable.strength_vlevo_vpravo),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Боковая планка", 45000, R.drawable.strength_oopa),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Приседания с поднятием штанги", 60000, R.drawable.strength_girya),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Выпады с гирями", 60000, R.drawable.strength_ruki_bazuki),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Жим штанги вверх", 45000, R.drawable.strength_oopa_girya)
        )
    ),

    Cardio(
        listOf(
            TrainingAction("Приготовьтесь", 10000, R.drawable.waiting),
            TrainingAction("Приседания", 30000, R.drawable.cardio_prisedanya),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Выпады", 30000, R.drawable.cardio_calenca_prisedanya),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Скручивания на пресс", 30000, R.drawable.cardio_lezhaty),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Отжимания", 30000, R.drawable.cardio_otgumanyia),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Выпрямление ног", 30000, R.drawable.cardio_leg),
            TrainingAction("Перерыв", 180000, R.drawable.waiting),
            TrainingAction("Подъем ног лежа", 30000, R.drawable.cardio_legs)
        )
    )
}