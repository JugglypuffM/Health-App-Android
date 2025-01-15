package KotlinAndroidApp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.JoggingViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class JoggingActivity: AppCompatActivity(){
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val ACCESS_ON_USE = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogging)
        supportActionBar?.hide()

        if (!hasFineLocationPermission()) {
            requestFineLocationPermission()
        } else {
            create()
        }
    }

    private fun create(){
        val distanceTextView = findViewById<TextView>(R.id.DistanceText)
        val clockTextView = findViewById<TextView>(R.id.ClockText)
        val timerDescriptionView = findViewById<TextView>(R.id.ClockDescription)
        val descriptionView = findViewById<TextView>(R.id.Descirption)
        val finishButton = findViewById<Button>(R.id.FinishButton)

        val mainApplication = application as MainApplication

        val viewModel = JoggingViewModel(
            application,
            mainApplication.xmlReader,
            mainApplication.logger,
            mainApplication.trainingHistory.value::add,
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

        viewModel.distanceMeters.observe(this, Observer { meters ->
            val longMeter = meters.toLong()
            distanceTextView.text = "${longMeter}m"
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this@JoggingActivity, message, Toast.LENGTH_SHORT).show()
        })

        finishButton.setOnClickListener{
            viewModel.onFinish()
            finish()
        }
    }

    private fun hasFineLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[0] == ACCESS_ON_USE)) {
                create()
            }
            else{
                finish()
            }
        }
    }
}

