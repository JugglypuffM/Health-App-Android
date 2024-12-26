import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.kotlin_android_app.R
import domain.training.Training
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.DurationUnit

class TrainingAdapter(private val trainings: List<Training>) : RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {

    // Создаём ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)
        return ViewHolder(view)
    }

    // Привязываем данные к ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val training = trainings[position]
        holder.bind(training)
    }

    // Возвращаем количество элементов
    override fun getItemCount(): Int {
        return trainings.size
    }

    // ViewHolder для элемента списка
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTrainingTitle: TextView = itemView.findViewById(R.id.tvTrainingTitle)
        private val tvTrainingTime: TextView = itemView.findViewById(R.id.tvTrainingTime)
        private val tvTrainingDuration: TextView = itemView.findViewById(R.id.tvTrainingDuration)

        // Метод для привязки данных
        fun bind(training: Training) {
            tvTrainingTitle.text = training.title

            // Форматируем дату и время
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val formattedDate = training.date.toJavaLocalDate().format(formatter)
            tvTrainingTime.text = formattedDate

            // Форматируем продолжительность
            val durationInMinutes = training.duration.toDouble(DurationUnit.MINUTES).toInt()
            tvTrainingDuration.text = "Продолжительность $durationInMinutes минут"
        }
    }
}