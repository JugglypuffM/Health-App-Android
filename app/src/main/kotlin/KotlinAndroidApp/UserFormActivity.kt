package KotlinAndroidApp

import UserFormViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R

class UserFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_form)

        val button: Button = findViewById(R.id.btn_submit)
        val nameField: EditText = findViewById(R.id.et_name)
        val weightField: EditText = findViewById(R.id.et_weight)
        val ageField: EditText = findViewById(R.id.et_age)
        val mainApplication = application as MainApplication

        val viewModel = UserFormViewModel(
            mainApplication.user,
            mainApplication.validator
        )

        viewModel.errorMessage.observe(this, Observer { message: String ->
            Toast.makeText(this@UserFormActivity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.onSuccess.observe(this, Observer {
            startActivity(Intent(this@UserFormActivity, HomeScreenActivity::class.java))
        })

        button.setOnClickListener {
            val name = nameField.text.toString()
            val age = ageField.text.toString()
            val weight = weightField.text.toString()

            viewModel.check(name, age, weight)
        }
    }
}