package viewadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.project.kotlin_android_app.R
import domain.training.Training
import domain.training.TrainingHistory
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TrainingAdapter(
    lifecycleOwner: LifecycleOwner,
    private val trainingHistory: LiveData<TrainingHistory>
) : RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {
    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val durationTextView: TextView = itemView.findViewById(R.id.trainingDuration)
        private val dataTextView: TextView = itemView.findViewById(R.id.trainingDate)

        open fun bind(item: Training){
            val time = LocalTime.ofSecondOfDay(item.duration.inWholeSeconds)
            val formatterTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            durationTextView.text = "Продолжительность ${formatterTime}"

            val javaLocalDate = item.date.toJavaLocalDate()
            dataTextView.text = javaLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }

    class YogaViewHolder(itemView: View) : ViewHolder(itemView)
    class PlankViewHolder(itemView: View) : ViewHolder(itemView)
    class DistanceViewHolder(itemView: View): ViewHolder(itemView) {
        private val distanceTextView = itemView.findViewById<TextView>(R.id.trainingDistance)

        override fun bind(item: Training){
            super.bind(item)
            if(item is Training.Jogging) {
                val meterDistance = item.distance.toLong()
                distanceTextView.text = "Протяжённость: ${meterDistance} метров"
            }
        }
    }

    private var items: List<Training>;

    init {
        items = trainingHistory.value?.value!!

        trainingHistory.observe(lifecycleOwner, Observer { trainingHistory ->
            items = trainingHistory.value.reversed()
            notifyDataSetChanged()
        })
    }

    override fun getItemViewType(position: Int): Int {
        return when (items.get(position)) {
            is Training.Yoga -> YOGA_TYPE
            is Training.Jogging -> JOGGING_TYPE
            is Training.Plank -> PLANK_TYPE
            null -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            YOGA_TYPE -> YogaViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_yoga, parent, false)
            )
            JOGGING_TYPE -> DistanceViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_jogging, parent, false)
            )
            PLANK_TYPE -> PlankViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_plank, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    companion object {
        private const val YOGA_TYPE = 0
        private const val JOGGING_TYPE = 1
        private const val PLANK_TYPE = 2
    }
}