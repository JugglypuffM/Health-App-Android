package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.TrainingViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Активность тренировки (таймер на выполнение списка действий тренировки)
 */
class TrainingActivity : AppCompatActivity() {

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        supportActionBar?.hide()

        val image: ImageView = findViewById(R.id.image)
        val titleText: TextView = findViewById(R.id.messageText)
        val timerText: TextView = findViewById(R.id.timerText)
        val cancelButton: Button = findViewById(R.id.startStopButton)

        val mainApplication = application as MainApplication

        val viewModel = TrainingViewModel(
            mainApplication.currentTraining!!
        )

        viewModel.onSuccess.observe(this, Observer {
            startActivity(Intent(this@TrainingActivity, HomeScreenActivity::class.java))
        })

        viewModel.currentAction.observe(this, Observer { action ->
            titleText.text = action.title
            image.setImageResource(action.imageSource)
        })

        viewModel.millisUntilFinished.observe(this, Observer { millisUntilFinished ->
            val duration: Duration = millisUntilFinished.milliseconds
            val minutes = duration.inWholeMinutes % 60
            val seconds = duration.inWholeSeconds % 60
            timerText.text = String.format("%02d:%02d", minutes, seconds)
        })

        cancelButton.setOnClickListener {
            viewModel.cancel()
        }

        viewModel.start()
    }
}