package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import auth.Authenticator
import com.project.kotlin_android_app.R
import domain.User
import domain.flatMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import viewmodel.ViewModelProvider

/**
 * Активность загрузки
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProvider.setContext(applicationContext)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            val result: Result<User> = ViewModelProvider.loadUser().flatMap { user ->
                ViewModelProvider.login(user.login, user.password).map {
                    user
                }
            }

            withContext(Dispatchers.Main) {
                result.onSuccess { user ->
                    val userProfileIntent = Intent(this@SplashActivity, UserProfileActivity::class.java)
                    userProfileIntent.putExtra("EXTRA_USER", user)
                    startActivity(userProfileIntent)
                }
                result.onFailure { error ->
                    val loginIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                    loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(loginIntent)
                    finish()
                    when (error) {
                        is Authenticator.ServerConnectionException -> {
                            Toast.makeText(
                                this@SplashActivity,
                                "Ошибка соединения с сервером",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Authenticator.InvalidCredentialsException -> {
                            ViewModelProvider.dropUser()
                        }
                    }
                }
            }
        }

        Log.d("SplashActivity", "onCreate: finished")
    }
}