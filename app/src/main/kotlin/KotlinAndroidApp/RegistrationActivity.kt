package KotlinAndroidApp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.kotlin_android_app.R

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
            val name = nameField.text.toString()
            val login = loginField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(login) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Успешная регистрация", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java).apply {
                    putExtra("EXTRA_NAME", name)
                    putExtra("EXTRA_LOGIN", login)
                    putExtra("EXTRA_PASSWORD", password)
                }
                startActivity(intent)
            }
        }
    }
}
