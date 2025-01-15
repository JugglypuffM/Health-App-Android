package KotlinAndroidApp

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kotlin_android_app.R
import domain.training.Containers
import domain.training.Icon
import utils.CircularList
import utils.CustomLogger
import utils.XMLReader

@SuppressLint("DiscouragedApi")
class HomeScreenViewModel(
    context: Application,
    xmlReader: XMLReader,
    logger: CustomLogger,
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