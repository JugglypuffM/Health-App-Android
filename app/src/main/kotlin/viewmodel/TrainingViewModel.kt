package viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.raise.result
import domain.training.Training
import domain.training.TrainingAction
import domain.training.TrainingActions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import services.training.TrainingService
import utils.TimerChain
import kotlin.time.Duration.Companion.milliseconds

class TrainingViewModel(
    private val trainingActions: TrainingActions,
    private val addTrainingHistory: (Training) -> Unit,
    private val trainingService: TrainingService
): ViewModel() {

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
        CoroutineScope(Dispatchers.IO).launch {
            val finishTimeMillis = System.currentTimeMillis()
            val trainingTimeMillis = finishTimeMillis - startTimeMillis

            val duration = trainingTimeMillis.milliseconds
            val date: LocalDate =
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val training = when (trainingActions) {
                TrainingActions.Yoga -> Training.Yoga(date, duration)
                TrainingActions.FullBodyStrength -> Training.FullBodyStrength(date, duration)
                TrainingActions.Cardio -> Training.Cardio(date, duration)
            }

            val sendTrainingResult = trainingService.saveTraining(training)

            sendTrainingResult.onFailure { error ->
                Log.d("TSLA", error.toString())
            }

            sendTrainingResult.onSuccess {
                Log.d("TSLA", "Training sent successfully")
            }

            withContext(Dispatchers.Main) {
                addTrainingHistory(training)
                _onSuccess.value = Unit
            }
        }
    }
}