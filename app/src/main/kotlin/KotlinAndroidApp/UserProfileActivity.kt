package KotlinAndroidApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.kotlin_android_app.R

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val userNameTextView: TextView = findViewById(R.id.user_name)
        val userLoginTextView: TextView = findViewById(R.id.user_login)
        val logoutButton: Button = findViewById(R.id.btn_logout)

        val userName = intent.getStringExtra("EXTRA_NAME")
        val userLogin = intent.getStringExtra("EXTRA_LOGIN")

        userNameTextView.text = "Имя пользователя: $userName"
        userLoginTextView.text = "Логин: $userLogin"

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
