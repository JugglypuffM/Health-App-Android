package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.kotlin_android_app.R
import viewmodel.SplashViewModel

/**
 * Активность загрузки
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val mainApplication: MainApplication = application as MainApplication;
        val viewModel = SplashViewModel(
            mainApplication.userSerializer,
            mainApplication.authenticator,
            mainApplication.user,
            mainApplication::createServicesStub
        )

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.onSuccess.observe(this, Observer {
            startActivity(Intent(this@SplashActivity, HomeScreenActivity::class.java))
        })

        viewModel.onFailure.observe(this, Observer {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        })

        viewModel.start()
    }
}