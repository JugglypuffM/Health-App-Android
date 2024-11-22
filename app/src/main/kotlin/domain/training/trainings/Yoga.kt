package domain.training.trainings

import domain.training.TrainingInfo

/**
 * Информация о деятельности "йога"
 */
class Yoga: TrainingInfo {
    override fun getTitle() = "Йога"
    override fun getDescription() = "Йога - это форма физических упражнений, которая способствует расслаблению, обретению равновесия и осознанности."
}