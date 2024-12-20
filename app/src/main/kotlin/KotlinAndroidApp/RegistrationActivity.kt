package KotlinAndroidApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import arrow.core.raise.result
import com.project.kotlin_android_app.R
import domain.exceptions.Exceptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.Validator

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

        val viewModel = (application as MainApplication).viewModel

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
                val result = result {
                    val user = viewModel.validate(inputName, inputLogin, inputPassword, inputConfirmPassword).bind()
                    Log.d("ATH", "user $user is correct")
                    viewModel.register(user.name!!, user.login, user.password).bind()
                    Log.d("ATH", "user $user correct register")
                    viewModel.saveUser(user).bind()
                    Log.d("ATH", "user $user saved")
                    user
                }

                withContext(Dispatchers.Main) {
                    result.onSuccess{ user ->
                        val userProfileIntent = Intent(this@RegistrationActivity, UserProfileActivity::class.java)
                        userProfileIntent.putExtra("EXTRA_USER", user)
                        startActivity(userProfileIntent)
                    }
                    result.onFailure { error ->
                        val message = when (error) {
                            is Validator.InvalidNameException -> "Неверное имя пользователя"
                            is Validator.InvalidLoginException -> "Неверный логин"
                            is Validator.InvalidPasswordException -> "Неверный пароль"
                            is Validator.NotEqualPasswordException -> "Пароли не совпадают"
                            is Exceptions.ServerConnectionException -> "Нет подключения к серверу"
                            is Exceptions.InvalidCredentialsException -> "Пользователь не найден"
                            is Exceptions.UserAlreadyExistsException -> "Пользователь с таким логином уже существует"
                            else -> "Непредвиденная ошибка"
                        }

                        Log.e("ATH", "throw user error: $error")
                        Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
