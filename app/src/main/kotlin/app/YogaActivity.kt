package app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.YogaViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Активность тренировки (таймер на выполнение списка действий тренировки)
 */
class YogaActivity : AppCompatActivity() {

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        supportActionBar?.hide()

        val image: ImageView = findViewById(R.id.image)
        val titleText: TextView = findViewById(R.id.Descirption)
        val timerText: TextView = findViewById(R.id.ClockText)
        val cancelButton: Button = findViewById(R.id.startStopButton)

        val mainApplication = application as MainApplication
        val viewModel = YogaViewModel(
            mainApplication.trainingHistory,
            mainApplication.trainingService!!,
            mainApplication.logger,
            mainApplication.xmlReader,
            application
        )

        viewModel.onError.observe(this, Observer {
            startActivity(Intent(this@YogaActivity, HomeScreenActivity::class.java))
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this@YogaActivity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.onFinish.observe(this, Observer {
            finish()
        })

        viewModel.currentAction.observe(this, Observer { action ->
            titleText.text = action.title
            image.setImageResource(action.imageResId)
        })

        viewModel.millisUntilTrainingFinished.observe(this, Observer { millisUntilFinished ->
            val duration: Duration = millisUntilFinished.milliseconds
            val minutes = duration.inWholeMinutes % 60
            val seconds = duration.inWholeSeconds % 60
            timerText.text = String.format("%02d:%02d", minutes, seconds)
        })

        cancelButton.setOnClickListener {
            viewModel.cancel()
        }
    }
}