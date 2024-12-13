package KotlinAndroidApp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import domain.training.Training

class HomeScreenViewModel(application: Application) : ViewModel() {

    private val mainApplication = application as MainApplication
    private val _trainings: List<Training> = mainApplication.trainings

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