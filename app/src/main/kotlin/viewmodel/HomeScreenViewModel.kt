package KotlinAndroidApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import domain.training.Training
import domain.training.Training.Yoga
import domain.training.Training.Jogging
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.minutes

class HomeScreenViewModel : ViewModel() {

    private val _trainings = listOf(
        Yoga(LocalDate(2023, 10, 1), 60.minutes),
        Jogging(LocalDate(2023, 10, 2), 30.minutes, 5.0)
    )

    private val _currentTrainingIndex = MutableLiveData(0)
    val currentTrainingIndex: LiveData<Int> get() = _currentTrainingIndex

    val currentTraining: LiveData<Training> = _currentTrainingIndex.switchMap { index ->
        MutableLiveData(_trainings[index])
    }

    fun nextTraining() {
        _currentTrainingIndex.value = (_currentTrainingIndex.value!! + 1) % _trainings.size
    }

    fun previousTraining() {
        _currentTrainingIndex.value = (_currentTrainingIndex.value!! - 1 + _trainings.size) % _trainings.size
    }
}