package config

import android.content.res.Resources

/**
 * Класс содержащий различные конфигурации приложения
 * @param resource доступ к ресурсам приложения
 */
class Config(resources: Resources) {
    val serverConfig: ServerConfig = ServerConfig(resources)
}