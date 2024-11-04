package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import arrow.core.raise.result
import auth.Authenticator
import com.project.kotlin_android_app.R
import domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Активность загрузки
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModel = (application as MainApplication).viewModel

        CoroutineScope(Dispatchers.IO).launch {
            val result = result {
                val account = viewModel.loadUser().bind()
                viewModel.login(account.login, account.password).bind()
                val basicUserData = viewModel.getBasicUserData(account.login, account.password).bind()
                User(basicUserData.name, account.login, account.password)
            }

            withContext(Dispatchers.Main) {
                result.onSuccess { user ->
                    val userProfileIntent = Intent(this@SplashActivity, UserProfileActivity::class.java)
                    userProfileIntent.putExtra("EXTRA_USER", user)
                    startActivity(userProfileIntent)
                }
                result.onFailure { error ->
                    val loginIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(loginIntent)
                    when (error) {
                        is Authenticator.ServerConnectionException -> {
                            Toast.makeText(
                                this@SplashActivity,
                                "Ошибка соединения с сервером",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Authenticator.InvalidCredentialsException -> {
                            viewModel.dropUser()
                        }

                        else ->{
                            Log.e("Unexpected error", "Unexpected error on splash activity, error: $error")
                        }
                    }
                }
            }
        }

        Log.d("SplashActivity", "onCreate: finished")
    }
}