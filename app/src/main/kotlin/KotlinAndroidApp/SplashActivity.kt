package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import arrow.core.raise.result
import com.project.kotlin_android_app.R
import domain.User
import domain.exceptions.Exceptions
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
                Log.d("ATH","Successfully loaded user $account")
                viewModel.login(account.login, account.password).bind()
                Log.d("ATH","Successfully login into account")
                val basicUserData = viewModel.getBasicUserData(account.login, account.password).bind()
                Log.d("ATH","Successfully get data: $basicUserData")
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
                        is Exceptions.ServerConnectionException -> {
                            Toast.makeText(
                                this@SplashActivity,
                                "Ошибка соединения с сервером",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Exceptions.InvalidCredentialsException -> {
                            viewModel.dropUser()
                        }
                    }
                    Log.d("ATH", "throw user error: $error")
                }
            }
        }

        Log.d("SplashActivity", "onCreate: finished")
    }
}