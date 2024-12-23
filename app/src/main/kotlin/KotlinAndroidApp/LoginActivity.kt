package KotlinAndroidApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.LoginViewModel

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

        val mainApplication: MainApplication = application as MainApplication;
        val viewModel = LoginViewModel(
            mainApplication.authenticator,
            mainApplication.userSerializer,
            mainApplication.user,
            mainApplication.validator
        )

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.onSuccess.observe(this, Observer {
            startActivity(Intent(this@LoginActivity, UserProfileActivity::class.java))
        })

        textViewRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }

        loginButton.setOnClickListener {
            val login = loginField.text.toString()
            val password = passwordField.text.toString()

            viewModel.check(login, password)
        }
    }
}
