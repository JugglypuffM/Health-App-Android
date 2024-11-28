package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.kotlin_android_app.R
import domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Активность для отображения профиля пользователя.
 */
@Suppress("DEPRECATION")
class UserProfileActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val userNameTextView: TextView = findViewById(R.id.user_name)
        val userLoginTextView: TextView = findViewById(R.id.user_login)
        val logoutButton: Button = findViewById(R.id.btn_logout)

        val (account, userInfo) = intent.getSerializableExtra("EXTRA_USER") as User
        val viewModel = (application as MainApplication).viewModel

        userNameTextView.text = "Имя пользователя: ${userInfo.name ?: "Неизвестно"}"
        userLoginTextView.text = "Логин: ${account.login}"

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            viewModel.dropAccount()
        }
    }
}
