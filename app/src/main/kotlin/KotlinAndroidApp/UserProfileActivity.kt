package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import auth.Authenticator
import com.project.kotlin_android_app.R
import domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewmodel.ViewModel
import viewmodel.ViewModelProvider

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

        var user = intent.getSerializableExtra("EXTRA_USER") as User

        if(user.name == null){
            CoroutineScope(Dispatchers.IO).launch {
                val result = ViewModelProvider.getBasicUserData(user.login, user.password)

                result.onSuccess { basicUserData ->
                    //TODO обновлять через LiveMutable
                    user = User(basicUserData.name, user.login, user.password)
                    userNameTextView.text = "Имя пользователя: ${user.name ?: "Неизвестно"}"
                    userLoginTextView.text = "Логин: ${user.login}"
                }

                //TODO сделать что-нибудь с ошибками
            }
        }

        userNameTextView.text = "Имя пользователя: ${user.name ?: "Неизвестно"}"
        userLoginTextView.text = "Логин: ${user.login}"

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            ViewModelProvider.dropUser()
            startActivity(intent)
            finish()
        }
    }
}
