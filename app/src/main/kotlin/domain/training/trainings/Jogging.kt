package domain.training.trainings

import domain.training.TrainingInfo

/**
 * Информация о деятельности "бег"
 * @param distance - дистанция (в километрах)
 */
class Jogging(private val distance: Double): TrainingInfo {
    override fun getTitle() = "Бег"
    override fun getDescription() = "Бег помогает укрепить сердечно-сосудистую систему, развить выносливость, улучшить настроение и поддерживать физическую форму"
}