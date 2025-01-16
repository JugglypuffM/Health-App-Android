package app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.RegistrationViewModel

/**
 * Активность для регистрации пользователя
 */
class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val textViewLogin: TextView = findViewById(R.id.textview_login)
        val registerButton: Button = findViewById(R.id.registration_button)
        val loginField: EditText = findViewById(R.id.user_login2)
        val passwordField: EditText = findViewById(R.id.user_password2)
        val confirmPasswordField: EditText = findViewById(R.id.user_password3)

        val mainApplication: MainApplication = application as MainApplication;
        val viewModel = RegistrationViewModel(
            mainApplication.authenticator,
            mainApplication.userSerializer,
            mainApplication.user,
            mainApplication.validator,
            mainApplication::createServices,
            mainApplication.logger
        )

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.onFinish.observe(this, Observer {
            startActivity(Intent(this@RegistrationActivity, UserFormActivity::class.java))
        })

        textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        registerButton.setOnClickListener {
            val login = loginField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            viewModel.check(login, password, confirmPassword)
        }
    }
}
