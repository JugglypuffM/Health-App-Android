package config

import android.content.res.Resources
import com.project.kotlin_android_app.R

/**
 * Класс конфигурации сервера
 * @param resource доступ к ресурсам android приложения
 */
class ServerConfig(resource: Resources){
    val url: String = resource.getString(R.string.server_url)
    val port: Int = resource.getInteger(R.integer.server_port)
}