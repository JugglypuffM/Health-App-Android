package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import auth.Authenticator
import com.project.kotlin_android_app.R
import domain.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import viewmodel.ViewModel

/**
 * Активность загрузки
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            val result = ViewModel.loadUser().flatMap { user ->
                ViewModel.login(user.login, user.password)
            }

            withContext(Dispatchers.Main) {
                when (result) {
                    is Either.Right -> {
                        val userProfileIntent = Intent(this@SplashActivity, UserProfileActivity::class.java)
                        userProfileIntent.putExtra("EXTRA_USER", result.value)
                        startActivity(userProfileIntent)
                    }

                    is Either.Left -> {
                        val loginIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                        loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(loginIntent)
                        when (result.error) {
                            is Authenticator.ServerConnectionException -> {
                                Toast.makeText(
                                    this@SplashActivity,
                                    "Ошибка соединения с сервером",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Authenticator.InvalidCredentialsException -> {
                                ViewModel.dropUser()
                            }
                        }
                    }
                }
            }
        }

        Log.d("SplashActivity", "onCreate: finished")
    }
}