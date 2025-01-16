package KotlinAndroidApp

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kotlin_android_app.R
import domain.training.Containers
import domain.training.Icon
import domain.training.Training
import domain.training.TrainingHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import services.training.TrainingService
import utils.CircularList
import utils.CustomLogger
import utils.XMLReader

@SuppressLint("DiscouragedApi")
class HomeScreenViewModel(
    context: Application,
    xmlReader: XMLReader,
    logger: CustomLogger,
    private val trainingService: TrainingService,
    private val trainingHistory: MutableLiveData<TrainingHistory>
) : ViewModel() {

    private val _currentTrainingIcon = MutableLiveData<Icon>()
    val currentTrainingIcon: LiveData<Icon> = _currentTrainingIcon

    private val _onError = MutableLiveData<Unit>()
    val onError: LiveData<Unit> = _onError

    private var circleTrainingList = CircularList<Icon>(emptyList())

    init{
        try {
            val rawIconList = xmlReader.read(Containers.RawIconList::class.java, R.raw.icon_list)
            val iconList = rawIconList.items.map { rawIcon ->
                    Icon(
                        rawIcon.title,
                        rawIcon.description,
                        context.resources.getIdentifier(rawIcon.imageResId, "drawable", context.packageName),
                        Class.forName(rawIcon.activityClass)
                    )
                }
            circleTrainingList = CircularList(iconList)
            _currentTrainingIcon.value = circleTrainingList.current()
        } catch (error: Exception) {
            logger.logError(error.toString())
            _onError.value = Unit
        }

        CoroutineScope(Dispatchers.IO).launch {
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val numberOfDays = 30

            for (i in 0 until numberOfDays) {
                val date = currentDate.minus(i, DateTimeUnit.DAY)
                trainingService.getTrainings(date).map { trainings ->
                    withContext(Dispatchers.Main) {
                        trainingHistory.value = trainingHistory.value?.plus(trainings)
                    }
                }
            }
        }
    }

    fun nextTraining(){
        circleTrainingList.next();
        _currentTrainingIcon.value = circleTrainingList.current()
    }

    fun previousTraining(){
        circleTrainingList.previous();
        _currentTrainingIcon.value = circleTrainingList.current()
    }
}