package viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kotlin_android_app.R
import domain.training.Action
import domain.training.Training
import domain.training.TrainingHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import services.training.TrainingService
import utils.CustomLogger
import utils.TimerChain
import utils.XMLReader
import kotlin.time.Duration.Companion.milliseconds



@SuppressLint("DiscouragedApi")
class YogaViewModel(
    private val trainingLiveData: MutableLiveData<TrainingHistory>,
    private val trainingService: TrainingService,
    private val logger: CustomLogger,
    xmlReader: XMLReader,
    context: Application
): ViewModel() {

    @Root(name = "actionList")
    private data class RawActionList(
        @field:ElementList(inline = true, entry = "action")
        var items: MutableList<RawAction> = mutableListOf()
    )

    @Root(name = "action")
    private data class RawAction(
        @field:Element(name = "title")
        var title: String = "",

        @field:Element(name = "duration")
        var durationMilliseconds: Long = 0,

        @field:Element(name = "imageResId")
        var imageResId: String = ""
    )

    private val _onError = MutableLiveData<Unit>()
    val onError: LiveData<Unit> = _onError

    private val _millisUntilTrainingFinished = MutableLiveData<Long>()
    val millisUntilTrainingFinished: LiveData<Long> = _millisUntilTrainingFinished

    private val _currentAction = MutableLiveData<Action>()
    val currentAction: LiveData<Action> = _currentAction

    private val _onFinish = MutableLiveData<Unit>()
    val onFinish: LiveData<Unit> = _onFinish

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage 

    private var startTimeMillis: Long
    private lateinit var timerChain: TimerChain

    init {
        try{
            val rawActionList = xmlReader.read(RawActionList::class.java, R.raw.yoga_action_list)
            val actionList = rawActionList.items.map { rawAction ->
                Action(
                    rawAction.title,
                    rawAction.durationMilliseconds,
                    context.resources.getIdentifier(rawAction.imageResId, "drawable", context.packageName)
                )
            }

            timerChain = TimerChain(
                ::updateViewTimer,
                ::updateActivity,
                ::onFinish,
                actionList
            )
        } catch (error: Exception){
            logger.logError(error.toString())
            _onError.value = Unit
        }


        startTimeMillis = System.currentTimeMillis()
        timerChain.start()
    }

    fun updateViewTimer(millisUntilFinished: Long){
        _millisUntilTrainingFinished.value = millisUntilFinished
    }

    fun updateActivity(action: Action){
        _currentAction.value = action
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

            val training = Training.Yoga(date, duration)
            val sendTrainingResult = trainingService.saveTraining(training)
            
            withContext(Dispatchers.Main) {
                sendTrainingResult.onSuccess {
                    trainingLiveData.value = trainingLiveData.value!! + training
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