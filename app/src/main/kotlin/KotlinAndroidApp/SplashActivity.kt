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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import viewmodel.ViewModel
import kotlin.coroutines.CoroutineContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            val user = ViewModel.storage.getUser()
            val result = ViewModel.authenticator.login(user.login, user.password)

            result.fold(
                onSuccess = {
                    val intent = Intent(this@SplashActivity, UserProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra("EXTRA_USER", user)
                    startActivity(intent)
                },
                onFailure = { error ->
                    when (error) {
                        is Authenticator.InvalidCredentialsException -> {
                            ViewModel.storage.dropUser()
                        }

                        is Authenticator.ServerConnectionException -> {
                            val message = "Ошибка соединения с сервером"
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    //TODO не должно возвращаться на экран загрузки
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            )
        }

        Log.d("SplashActivity", "onCreate: finished")
    }
}