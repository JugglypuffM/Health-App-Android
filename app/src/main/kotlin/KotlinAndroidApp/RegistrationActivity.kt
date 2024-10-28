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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import viewmodel.ViewModel
import domain.Either
import domain.Validate

/**
 * Активность для регистрации пользователя
 */
class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val textViewLogin: TextView = findViewById(R.id.textview_login)
        val registerButton: Button = findViewById(R.id.registration_button)
        val nameField: EditText = findViewById(R.id.user_login3)
        val loginField: EditText = findViewById(R.id.user_login2)
        val passwordField: EditText = findViewById(R.id.user_password2)
        val confirmPasswordField: EditText = findViewById(R.id.user_password3)

        textViewLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val inputName = nameField.text.toString()
            val inputLogin = loginField.text.toString()
            val inputPassword = passwordField.text.toString()
            val inputConfirmPassword = confirmPasswordField.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val result = ViewModel.validate(inputName, inputLogin, inputPassword, inputConfirmPassword).flatMap { user ->
                    ViewModel.register(user.name!!, user.login, user.password)
                }
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Either.Right -> {
                            val userProfileIntent = Intent(this@RegistrationActivity, UserProfileActivity::class.java)
                            userProfileIntent.putExtra("EXTRA_USER", result.value)
                            startActivity(userProfileIntent)
                        }

                        is Either.Left -> {
                            val loginIntent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                            val message = when (result.error) {
                                is Validate.InvalidNameException -> "Неверное имя пользователя"
                                is Validate.InvalidLoginException -> "Неверный логин"
                                is Validate.InvalidPasswordException -> "Неверный пароль"
                                is Validate.NotEqualPasswordException -> "Пароли не совпадают"
                                is Authenticator.ServerConnectionException -> "Нет подключения к серверу"
                                is Authenticator.InvalidCredentialsException -> "Пользователь не найден"
                                is Authenticator.UserAlreadyExistsException -> "Пользователь с таким логином уже существует"
                                else -> "Непредвиденная ошибка"
                            }
                            Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
