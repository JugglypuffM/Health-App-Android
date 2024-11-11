package domain.training


/**
 * Класс информации о тренировке
 * @param name название тренировки
 */
//TODO по усмотрению тут можно хранить иконку в формате Int (R.drawable.ic_yoga, R.drawable.ic_walk, ...)
//TODO так же сюда можно определить расход калорий за единицу времени для конкретной тренировки
sealed class TrainingDescription(name: String){
    class YogaDescription : TrainingDescription("Йога")
}