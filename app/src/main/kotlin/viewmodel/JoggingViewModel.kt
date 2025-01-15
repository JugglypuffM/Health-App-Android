package viewmodel

import android.app.Application
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kotlin_android_app.R
import domain.training.Training
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import services.training.TrainingService
import utils.CustomLogger
import utils.DistanceTracker
import utils.XMLReader
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


class JoggingViewModel(
    context: Application,
    xmlReader: XMLReader,
    private val logger: CustomLogger,
    private val addTrainingHistory: (Training) -> Unit,
    private val trainingService: TrainingService
): ViewModel() {

    @Root(name = "container")
    private class Container(
        @field:Element(name = "countdownAction")
        var countdownAction: CountdownAction = CountdownAction(),

        @field:Element(name = "stopwatchAction")
        var stopwatchAction: StopwatchAction = StopwatchAction(),
    )

    @Root(name = "countdownAction")
    private class CountdownAction(
        @field:Element(name = "title")
        var title: String = "",

        @field:Element(name = "timerDescription")
        var timerDescription: String = "",

        @field:Element(name = "duration")
        var duration: Long = 0,
    )

    @Root(name = "stopwatchAction")
    private class StopwatchAction(
        @field:Element(name = "title")
        var title: String = "",

        @field:Element(name = "timerDescription")
        var timerDescription: String = ""
    )

    private val _descriptionMessage = MutableLiveData<String>()
    val descriptionMessage: LiveData<String> = _descriptionMessage

    private val _timerDescriptionMessage = MutableLiveData<String>()
    val timerDescriptionMessage: LiveData<String> = _timerDescriptionMessage

    private val _countDownSeconds = MutableLiveData<Long>()
    val countDownSeconds: LiveData<Long> = _countDownSeconds

    private val _stopwatchSeconds = MutableLiveData<Long>()
    val stopwatchSeconds: LiveData<Long> = _stopwatchSeconds

    private val _durationMilliseconds = MutableLiveData<Long>()
    val durationMilliseconds: LiveData<Long> = _durationMilliseconds

    private val _distanceMeters = MutableLiveData<Double>()
    val distanceMeters: LiveData<Double> = _distanceMeters
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _onError = MutableLiveData<Unit>()
    val onError: LiveData<Unit> = _onError

    private val _onFinish = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish

    private var handler = Handler(Looper.myLooper()!!)
    private var stopwatchTime = 0L

    private lateinit var countdownAction: CountdownAction
    private lateinit var stopwatchAction: StopwatchAction

    private lateinit var distanceTracker: DistanceTracker

    init {
        distanceTracker = DistanceTracker(context){ distanceMeter ->
            _distanceMeters.value = distanceMeter
        }

        try {
            val container = xmlReader.read(Container::class.java, R.raw.jogging_action)

            countdownAction = container.countdownAction
            stopwatchAction = container.stopwatchAction
            startCountdown()
        } catch (error: Exception){
            _onError.value = Unit
            logger.logError(error.toString())
        }
    }

    private fun startCountdown() {
        _descriptionMessage.value = countdownAction.title
        _timerDescriptionMessage.value = countdownAction.timerDescription

        object : CountDownTimer(countdownAction.duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _countDownSeconds.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                startStopwatch()
            }
        }.start()
    }

    private fun startStopwatch() {
        _descriptionMessage.value = stopwatchAction.title
        _timerDescriptionMessage.value = stopwatchAction.timerDescription
        _stopwatchSeconds.value = 0
        distanceTracker.startTracking()

        handler.post(object : Runnable {
            override fun run() {
                _stopwatchSeconds.value = stopwatchTime
                stopwatchTime++
                handler.postDelayed(this, 1000)
            }
        })
    }

    fun onFinish(){
        distanceTracker.stopTracking()
        
        CoroutineScope(Dispatchers.IO).launch {
            val duration: Duration = (_durationMilliseconds.value ?: 0.0).toLong().milliseconds
            val distance: Double = _distanceMeters.value ?: 0.0
            val date: LocalDate =
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            val training = Training.Jogging(date, duration, distance)
            val sendTrainingResult = trainingService.saveTraining(training)

            withContext(Dispatchers.Main) {
                sendTrainingResult.onSuccess {
                    addTrainingHistory(training)
                }

                sendTrainingResult.onFailure { error ->
                    _errorMessage.value = "Ошибка отправки тренировки на сервер"
                    _errorMessage.value = "Тренировка не была сохранена"
                    logger.logDebug(error.toString())
                }

                _onFinish.value = Unit
            }
        }
    }
}