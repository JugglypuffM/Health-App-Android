package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import domain.training.Training
import domain.training.TrainingAction
import domain.training.TrainingActions
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import utils.TimerChain
import kotlin.time.Duration.Companion.milliseconds

class TrainingViewModel(val trainingActions: TrainingActions, val addTrainingHistory: (Training) -> Unit): ViewModel() {

    private val _millisUntilTrainingFinished = MutableLiveData<Long>()
    val millisUntilTrainingFinished: LiveData<Long> = _millisUntilTrainingFinished

    private val _currentAction = MutableLiveData<TrainingAction>()
    val currentAction: LiveData<TrainingAction> = _currentAction

    private val _onSuccess = MutableLiveData<Unit>()
    val onSuccess: LiveData<Unit> = _onSuccess

    private var startTimeMillis: Long = 0

    private val timerChain = TimerChain(
        ::updateViewTimer,
        ::updateActivity,
        ::onFinish,
        trainingActions.value
    )

    fun updateViewTimer(millisUntilFinished: Long){
        _millisUntilTrainingFinished.value = millisUntilFinished
    }

    fun updateActivity(action: TrainingAction){
        _currentAction.value = action
    }

    fun start(){
        startTimeMillis = System.currentTimeMillis()
        timerChain.start()
    }

    fun cancel(){
        timerChain.cancel()
    }

    fun onFinish(){
        val finishTimeMillis = System.currentTimeMillis()
        val trainingTimeMillis = finishTimeMillis - startTimeMillis

        val duration = trainingTimeMillis.milliseconds
        val date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        addTrainingHistory(when(trainingActions){
            TrainingActions.Yoga -> Training.Yoga(date, duration)
            TrainingActions.FullBodyStrength -> Training.FullBodyStrength(date, duration)
            TrainingActions.Cardio -> Training.Cardio(date, duration)
        })
        _onSuccess.value = Unit
    }
}