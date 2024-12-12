package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import utils.TimerChain
import domain.training.TrainingAction

import androidx.appcompat.app.AppCompatActivity
import com.project.kotlin_android_app.R
import domain.training.TrainingActions
import java.time.Duration

/**
 * Активность тренировки (таймер на выполнение списка действий тренировки)
 */
class TrainingActivity : AppCompatActivity() {

    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        supportActionBar?.hide()

        val image: ImageView = findViewById(R.id.image)
        val titleText: TextView = findViewById(R.id.messageText)
        val timerText: TextView = findViewById(R.id.timerText)
        val startStopButton: Button = findViewById(R.id.startStopButton)


        val timerChain = TimerChain(
            updateViewTimer = { millisUntilFinished: Long ->
                val duration = Duration.ofMillis(millisUntilFinished)
                val minutes = duration.toMinutesPart()
                val seconds = duration.toSecondsPart()
                timerText.text = String.format("%02d:%02d", minutes, seconds)
            },
            updateActivity = { action: TrainingAction ->
                titleText.text = action.title
                image.setImageResource(action.imageSource)
            },
            finish = {
                //save training to history
                //callback to the activity
            },
            training = TrainingActions.Yoga.actions
        )

        startStopButton.setOnClickListener{
            timerChain.cancel()
        }
    }
}