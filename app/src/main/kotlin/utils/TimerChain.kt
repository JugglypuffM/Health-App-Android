package utils

import android.os.CountDownTimer
import domain.training.TrainingAction

/**
 * Цепочка таймеров
 * @param updateViewTimer функция для отображения таймера (вызывается каждую секунду)
 * @param updateActivity функция для обновления активности (вызывается по завершению n-ого таймера)
 * @param finish функция вызывающаяся при истечении всех таймеров
 * @param training параметры таймеров
 */
class TimerChain(val updateViewTimer: (millisUntilFinished: Long) -> Unit, val updateActivity: (trainingAction: TrainingAction) -> Unit, val finish: () -> Unit, training: Iterable<TrainingAction>) {
    private val trainingIterator = training.iterator()
    private var timer: CountDownTimer? = null

    init {
        onFinish()
    }

    private fun startTimer(millisUntilFinished: Long) {
        timer = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long){
                this@TimerChain.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                this@TimerChain.onFinish()
            }
        }

        timer?.start();
    }

    private fun onTick(millisUntilFinished: Long){
        updateViewTimer(millisUntilFinished)
    }

    private fun onFinish(){
        if(!trainingIterator.hasNext()){
            finish()
        }
        else {
            val action = trainingIterator.next()
            updateActivity(action)
            startTimer(action.timeUntilMillis)
        }
    }

    /**
     * Остановить выполнение всех таймеров
     */
    fun cancel(){
        timer?.cancel()
        finish()
    }
}