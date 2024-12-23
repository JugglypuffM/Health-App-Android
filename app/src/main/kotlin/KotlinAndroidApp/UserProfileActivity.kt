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

/**
 * Активность для отображения профиля пользователя.
 */
class UserProfileActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val userNameTextView: TextView = findViewById(R.id.user_name)
        val userLoginTextView: TextView = findViewById(R.id.user_login)
        val logoutButton: Button = findViewById(R.id.btn_logout)

        val mainApplication = application as MainApplication
        val (account, userInfo) = mainApplication.user
        val viewModel = UserProfileViewModel(
            mainApplication.userSerializer,
            mainApplication.user
        )

        userNameTextView.text = "Имя пользователя: ${userInfo.name}"
        userLoginTextView.text = "Логин: ${account.login}"

        viewModel.onSuccess.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }
}
