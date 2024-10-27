package KotlinAndroidApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import auth.AuthenticationWithValidation
import auth.Authenticator
import auth.GrpcAuthenticatorStub
import com.project.kotlin_android_app.R
import domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.LocalDatabase

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            val authenticator = AuthenticationWithValidation(GrpcAuthenticatorStub())
            val storage = LocalDatabase()
            val user: User = storage.getUser()

            val result = authenticator.login(user.login, user.password)
            result.fold(
                onSuccess = {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                },
                onFailure = { error ->
                    val message = when (error) {
                        is Authenticator.InvalidCredentialsException -> "Неверный логин или пароль"
                        is Authenticator.UserAlreadyExistsException -> "Пользователь уже существует"
                        is Authenticator.ServerConnectionException -> "Ошибка связи с сервером"
                        else -> "Неизвестная ошибка"
                    }
                    Toast.makeText(this@SplashActivity, message, Toast.LENGTH_SHORT).show()
                }
            )
        }

    }
}