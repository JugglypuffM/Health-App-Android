package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewmodel.UserProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        val logoutButton: Button = findViewById(R.id.btn_logout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        editUserButton = findViewById(R.id.btn_edit_user)
        bottomNavigationView.selectedItemId = R.id.profile

        val mainApplication = application as MainApplication
        val (account, userInfo) = mainApplication.user
        val viewModel = UserProfileViewModel(
            mainApplication.userSerializer,
            mainApplication.user
        )

        userNameTextView.text = "Имя пользователя: ${userInfo.name}"
        userAgeTextView.text = "Возраст: ${userInfo.age} лет"
        userWeightTextView.text = "Вес: ${userInfo.weight} кг"

        viewModel.onSuccess.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        logoutButton.setOnClickListener {
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


