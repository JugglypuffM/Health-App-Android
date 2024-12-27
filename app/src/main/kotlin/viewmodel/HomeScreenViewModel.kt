package KotlinAndroidApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import domain.training.TrainingActions
import domain.training.TrainingIcon
import utils.CircularList

class HomeScreenViewModel(val setCurrentTrainings: (TrainingActions?) -> Unit) : ViewModel() {
    private val _currentTrainingIcon = MutableLiveData<TrainingIcon>()
    val currentTrainingIcon: LiveData<TrainingIcon> = _currentTrainingIcon

    private val circleTrainingList = CircularList(TrainingIcon.entries.toList())

    init{
        _currentTrainingIcon.value = circleTrainingList.current()
    }

    fun nextTraining(){
        circleTrainingList.next();
        _currentTrainingIcon.value = circleTrainingList.current()
    }

    fun previousTraining(){
        circleTrainingList.previous();
        _currentTrainingIcon.value = circleTrainingList.current()
    }

    fun setTrainingActions() {
        setCurrentTrainings(
            when(circleTrainingList.current()){
                TrainingIcon.Yoga -> TrainingActions.Yoga
                TrainingIcon.FullBodyStrength -> TrainingActions.FullBodyStrength
                TrainingIcon.Cardio -> TrainingActions.Cardio
        })
    }
}