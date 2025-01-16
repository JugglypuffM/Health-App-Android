package app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.UserProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Активность для отображения профиля пользователя.
 */
class UserProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var editUserButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val userNameTextView: TextView = findViewById(R.id.user_name)
        val userAgeTextView: TextView = findViewById(R.id.user_age)
        val userWeightTextView: TextView = findViewById(R.id.user_weight)
        val userDistanceTextView: TextView = findViewById(R.id.user_distance)
        val logoutButton: Button = findViewById(R.id.btn_logout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        editUserButton = findViewById(R.id.btn_edit_user)
        bottomNavigationView.selectedItemId = R.id.profile

        val mainApplication = application as MainApplication
        val viewModel = UserProfileViewModel(
            mainApplication.dataRequester,
            mainApplication.userSerializer,
            mainApplication.user.userInfo,
            mainApplication.logger
        )

        viewModel.userInfo.observe(this) { userInfo ->
            userNameTextView.text = "Имя пользователя: %s".format(userInfo.name)
            userAgeTextView.text = "Возраст: %d лет".format(userInfo.age)
            userWeightTextView.text = "Вес: %d кг".format(userInfo.weight)
            userDistanceTextView.text = "Дистанция: %d метров".format(userInfo.distance)
        }

        viewModel.errorMessage.observe(this) { message: String ->
            Toast.makeText(this@UserProfileActivity, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.onFinish.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        logoutButton.setOnClickListener {
            mainApplication.trainingHistory.value?.clear()
            viewModel.logout()
        }

        editUserButton.setOnClickListener {
            startActivity(Intent(this, UserFormActivity::class.java))
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, HomeScreenActivity::class.java))
                    true
                }
                R.id.profile -> true
                else -> false
            }
        }
    }
}


