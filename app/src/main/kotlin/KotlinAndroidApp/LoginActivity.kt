package KotlinAndroidApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.kotlin_android_app.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginField: EditText = findViewById(R.id.user_login)
        val passwordField: EditText = findViewById(R.id.user_password)
        val loginButton: Button = findViewById(R.id.button)
        val textViewRegister: TextView = findViewById(R.id.textview_register)

        val registeredLogin = intent.getStringExtra("EXTRA_LOGIN")
        val registeredPassword = intent.getStringExtra("EXTRA_PASSWORD")
        val registeredName = intent.getStringExtra("EXTRA_NAME")

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val inputLogin = loginField.text.toString()
            val inputPassword = passwordField.text.toString()

            if (inputLogin == registeredLogin && inputPassword == registeredPassword) {

                val intent = Intent(this, UserProfileActivity::class.java).apply {
                    putExtra("EXTRA_LOGIN", registeredLogin)
                    putExtra("EXTRA_NAME", registeredName)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
