package app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.PlankViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Активность стойки в планке
 */
class PlankActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plank)
        supportActionBar?.hide()

        val clockTextView = findViewById<TextView>(R.id.ClockText)
        val timerDescriptionView = findViewById<TextView>(R.id.ClockDescription)
        val descriptionView = findViewById<TextView>(R.id.Descirption)
        val finishButton = findViewById<Button>(R.id.FinishButton)

        val mainApplication = application as MainApplication

        val viewModel = PlankViewModel(
            mainApplication.xmlReader,
            mainApplication.logger,
            mainApplication.trainingHistory,
            mainApplication.trainingService!!
        )

        viewModel.onError.observe(this, Observer{
            finish()
        })

        viewModel.descriptionMessage.observe(this, Observer { message ->
            descriptionView.text = message
        })

        viewModel.timerDescriptionMessage.observe(this, Observer { message ->
            timerDescriptionView.text = message
        })

        viewModel.countDownSeconds.observe(this, Observer { seconds ->
            val time = LocalTime.ofSecondOfDay(seconds)
            clockTextView.text = time.format(DateTimeFormatter.ofPattern("ss"))
        })

        viewModel.stopwatchSeconds.observe(this, Observer { seconds ->
            val time = LocalTime.ofSecondOfDay(seconds)
            clockTextView.text = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this@PlankActivity, message, Toast.LENGTH_SHORT).show()
        })

        finishButton.setOnClickListener{
            viewModel.onFinish()
            finish()
        }
    }
}

