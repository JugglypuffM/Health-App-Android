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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import viewmodel.ViewModel

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

            if(inputPassword != inputConfirmPassword) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(inputName, inputLogin, inputPassword)

            CoroutineScope(Dispatchers.IO).launch {
                val result = ViewModel.authenticator.register(inputName, inputLogin, inputPassword)

                result.fold(
                    onSuccess = {
                        val intent = Intent(this@RegistrationActivity, UserProfileActivity::class.java)
                        intent.putExtra("EXTRA_USER", user)
                        startActivity(intent)
                        ViewModel.storage.saveUser(user)
                    },
                    onFailure = { error ->
                        val message = when (error) {
                            is Authenticator.ServerConnectionException -> "Ошибка соединения с сервером"
                            is Authenticator.UserAlreadyExistsException -> "Пользователь с таким логином уже существует"
                            else -> "Непредвиденная ошибка"
                        }

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}
