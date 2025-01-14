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
    )
}