package KotlinAndroidApp

import TrainingAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.kotlin_android_app.R
import domain.training.Icon

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var workoutImage: ImageView
    private lateinit var workoutName: TextView
    private lateinit var workoutDescription: TextView
    private lateinit var btnStartWorkout: Button
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnNext: ImageButton

    private lateinit var viewModel: HomeScreenViewModel
    private var activityClass: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        val mainApplication = application as MainApplication

        workoutImage = findViewById(R.id.ivWorkoutImage)
        workoutName = findViewById(R.id.tvWorkoutName)
        workoutDescription = findViewById(R.id.tvWorkoutDescription)
        btnStartWorkout = findViewById(R.id.btnStartWorkout)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        viewModel = HomeScreenViewModel(
            application,
            mainApplication.xmlReader,
            mainApplication.logger,
        )

        viewModel.onError.observe(this, Observer {
            startActivity(Intent(this@HomeScreenActivity, LoginActivity::class.java))
        })

        viewModel.currentTrainingIcon.observe(this, Observer { training: Icon ->
            workoutName.text = training.title
            workoutDescription.text = training.description
            workoutImage.setImageResource(training.imageResId)
            activityClass = training.activityClass
        })

        btnStartWorkout.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, activityClass))
        }

        btnPrevious.setOnClickListener {
            viewModel.previousTraining()
        }

        btnNext.setOnClickListener {
            viewModel.nextTraining()
        }

        val recyclerView: RecyclerView = findViewById(R.id.rvTrainings)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val trainings = mainApplication.trainingHistory
        val adapter = TrainingAdapter(this, trainings)

        recyclerView.adapter = adapter
    }
}