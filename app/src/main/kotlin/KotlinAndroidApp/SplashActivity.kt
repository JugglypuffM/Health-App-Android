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

        val mainApplication: MainApplication = application as MainApplication;
        val viewModel = mainApplication.viewModel

        CoroutineScope(Dispatchers.IO).launch {
            val result = result {
                val account = viewModel.loadAccount().bind()
                Log.d("ATH","Successfully loaded user $account")
                viewModel.login(account.login, account.password).bind()
                Log.d("ATH","Successfully login into account")
                val userInfo = viewModel.getUserData(account.login, account.password).bind()
                Log.d("ATH","Successfully get data: $userInfo")
                User(account, userInfo)
            }

            withContext(Dispatchers.Main) {
                result.onSuccess { user ->
                    mainApplication.user = user;
                    val userProfileIntent = Intent(this@SplashActivity, UserProfileActivity::class.java)
                    startActivity(userProfileIntent)
                    viewModel.saveAccount(user.account)
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
                            viewModel.dropAccount()
                        }
                    }
                    Log.d("ATH", "throw user error: $error")
                }
            }
        }

        Log.d("SplashActivity", "onCreate: finished")
    }
}