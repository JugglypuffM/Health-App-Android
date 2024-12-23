package KotlinAndroidApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.kotlin_android_app.R
import domain.training.Training

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var workoutImage: ImageView
    private lateinit var workoutName: TextView
    private lateinit var workoutDescription: TextView
    private lateinit var btnStartWorkout: Button
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton

    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        workoutImage = findViewById(R.id.ivWorkoutImage)
        workoutName = findViewById(R.id.tvWorkoutName)
        workoutDescription = findViewById(R.id.tvWorkoutDescription)
        btnStartWorkout = findViewById(R.id.btnStartWorkout)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        viewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)

        val mainApplication = application as MainApplication

        viewModel.currentTraining.observe(this, { training ->
            updateUI(training)
        })

        btnStartWorkout.setOnClickListener {
            viewModel.setCurrentTraining(mainApplication)
            startActivity(Intent(this@HomeScreenActivity, TrainingActivity::class.java))
        }

        btnPrevious.setOnClickListener {
            viewModel.previousTraining()
        }

        btnNext.setOnClickListener {
            viewModel.nextTraining()
        }
    }

    private fun updateUI(training: Training) {
        workoutName.text = training.title
        workoutDescription.text = training.description

        when (training) {
            is Training.Yoga -> workoutImage.setImageResource(R.drawable.ic_yoga)
            is Training.Jogging -> workoutImage.setImageResource(R.drawable.ic_running)
        }
    }
}