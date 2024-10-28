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
import domain.Either
import domain.User
import domain.Validate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import viewmodel.ViewModel

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

            val user = User(null, inputLogin, inputPassword)

            CoroutineScope(Dispatchers.IO).launch {
                val result = ViewModel.validate(inputLogin, inputPassword).flatMap { user ->
                    ViewModel.login(user.login, user.password)
                }

                withContext(Dispatchers.Main) {
                    when (result) {
                        is Either.Right -> {
                            val userProfileIntent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                            userProfileIntent.putExtra("EXTRA_USER", result.value)
                            startActivity(userProfileIntent)
                        }

                        is Either.Left -> {
                            val loginIntent = Intent(this@LoginActivity, LoginActivity::class.java)
                            val message = when (result.error) {
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
}
