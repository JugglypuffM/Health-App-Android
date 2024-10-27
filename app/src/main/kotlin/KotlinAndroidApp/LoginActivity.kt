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
                val result = ViewModel.authenticator.login(user.login, user.password)

                result.fold(
                    onSuccess = {
                        val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                        intent.putExtra("EXTRA_USER", user)
                        startActivity(intent)

                        ViewModel.storage.saveUser(user)
                    },
                    onFailure = {error ->
                        //TODO InvalidCredentialsException - это либо неверный логин либо не подходящий на сервере логин, что не очень то должно волновать, но всё же
                        //TODO упразднить AuthenticationWithValidation
                        val message = when (error) {
                            is Authenticator.InvalidCredentialsException -> "Неверный логин или пароль"
                            is Authenticator.ServerConnectionException -> "Ошибка соединения с сервером"
                            else -> "Непредвиденная ошибка"
                        }

                         withContext(Dispatchers.Main) {
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}
