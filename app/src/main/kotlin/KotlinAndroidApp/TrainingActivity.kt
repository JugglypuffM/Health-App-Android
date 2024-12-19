package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
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
import java.util.Locale

/**
 * Активность тренировки (таймер на выполнение списка действий тренировки)
 */
class TrainingActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

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
                speak(action.title);
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Установка языка
            val result = tts.setLanguage(Locale("ru", "RU"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("ERRM", "Язык не поддерживается или отсутствуют данные.")
            }
        } else {
            Log.d("ERRM", "Ошибка инициализации TTS")
        }
    }

    fun speak(message: String){
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}