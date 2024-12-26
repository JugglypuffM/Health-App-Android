package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import domain.training.TrainingAction
import domain.training.TrainingActions
import utils.TimerChain
import java.time.Duration

class TrainingViewModel(trainingActions: TrainingActions): ViewModel() {

    private val _millisUntilFinished = MutableLiveData<Long>()
    val millisUntilFinished: LiveData<Long> = _millisUntilFinished

    private val _currentAction = MutableLiveData<TrainingAction>()
    val currentAction: LiveData<TrainingAction> = _currentAction

    private val _onSuccess = MutableLiveData<Unit>()
    val onSuccess: LiveData<Unit> = _onSuccess

    private val timerChain = TimerChain(
        updateViewTimer = { millisUntilFinished ->
            _millisUntilFinished.value = millisUntilFinished
        },
        updateActivity = { action ->
            _currentAction.value = action
        },
        finish = {
            _onSuccess.value = Unit
        },
        training = trainingActions.value
    )

    fun start(){
        timerChain.start()
    }

    fun cancel(){
        timerChain.cancel()
    }
}