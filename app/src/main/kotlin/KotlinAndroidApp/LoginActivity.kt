package KotlinAndroidApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import auth.Authenticator
import com.project.kotlin_android_app.R
import domain.User
import domain.Validate
import domain.flatMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import viewmodel.ViewModelProvider

/**
 * Активность для входа в приложение
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginField: EditText = findViewById(R.id.user_login)
        val passwordField: EditText = findViewById(R.id.user_password)
        val loginButton: Button = findViewById(R.id.button)
        val textViewRegister: TextView = findViewById(R.id.textview_register)

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val inputLogin = loginField.text.toString()
            val inputPassword = passwordField.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val result: Result<User> = ViewModelProvider.validate(inputLogin, inputPassword).flatMap { user ->
                    ViewModelProvider.login(user.login, user.password).flatMap { _ ->
                        ViewModelProvider.saveUser(user).map { _ ->
                            user
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    result.onSuccess { user ->
                        val userProfileIntent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                        userProfileIntent.putExtra("EXTRA_USER", user)
                        startActivity(userProfileIntent)
                    }

                    result.onFailure { error ->
                        val message = when (error) {
                            is Validate.InvalidNameException -> "Неверное имя пользователя"
                            is Validate.InvalidLoginException -> "Неверный логин"
                            is Validate.InvalidPasswordException -> "Неверный пароль"
                            is Authenticator.ServerConnectionException -> "Нет подключения к серверу"
                            is Authenticator.InvalidCredentialsException -> "Пользователь не найден"
                            else -> "Непредвиденная ошибка"
                        }
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
